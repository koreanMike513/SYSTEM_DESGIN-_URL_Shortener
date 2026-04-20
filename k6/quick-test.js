import http from "k6/http";
import { check, sleep } from "k6";
import { Counter, Rate, Trend } from "k6/metrics";

// t3.xlarge 단일 인스턴스 기준 (MySQL + Redis + App 공존)
// 쓰기 100 TPS : 읽기 900 TPS → 9:1 유지
const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

const writeErrors  = new Counter("write_errors");
const readErrors   = new Counter("read_errors");
const writeLatency = new Trend("write_latency_ms", true);
const readLatency  = new Trend("read_latency_ms",  true);
const cacheHitRate = new Rate("cache_hit_rate");

export const options = {
  scenarios: {
    write_urls: {
      executor:        "ramping-arrival-rate",
      startRate:       0,
      timeUnit:        "1s",
      preAllocatedVUs: 30,
      maxVUs:          100,
      stages: [
        { target: 30,  duration: "1m" },   // ramp-up
        { target: 100, duration: "30s" },
        { target: 100, duration: "3m" },   // sustained
        { target: 0,   duration: "30s" },  // cooldown
      ],
      exec: "writeUrl",
    },
    read_urls: {
      executor:        "ramping-arrival-rate",
      startRate:       0,
      timeUnit:        "1s",
      preAllocatedVUs: 100,
      maxVUs:          500,
      stages: [
        { target: 200, duration: "1m" },
        { target: 900, duration: "30s" },
        { target: 900, duration: "3m" },
        { target: 0,   duration: "30s" },
      ],
      exec: "readUrl",
    },
  },

  thresholds: {
    "write_latency_ms": ["p(95)<500", "p(99)<1000"],
    "read_latency_ms":  ["p(95)<100", "p(99)<300"],
    "http_req_failed":  ["rate<0.01"],
    "write_errors":     ["count<50"],
    "read_errors":      ["count<200"],
  },
};

const SHORT_CODE_POOL = [];

// ── 테스트 전 seed: 읽기에서 사용할 short code 500개 생성 ──
export function setup() {
  console.log("[setup] Seeding 500 short codes...");
  for (let i = 0; i < 500; i++) {
    const res = http.post(`${BASE_URL}/api/v1/urls/shorten`,
      JSON.stringify({ originalURL: `https://seed.example.com/page/${i}`, alias: `seed-${i}` }),
      { headers: { "Content-Type": "application/json" } }
    );
    if (res.status === 200) {
      SHORT_CODE_POOL.push(`seed-${i}`);
    }
  }
  console.log(`[setup] Seeded ${SHORT_CODE_POOL.length} codes.`);
  return { codes: SHORT_CODE_POOL };
}

export function writeUrl() {
  const payload = JSON.stringify({
    originalURL: `https://example.com/${Math.random().toString(36).slice(2, 10)}`,
  });
  const start = Date.now();
  const res = http.post(`${BASE_URL}/api/v1/urls/shorten`, payload, {
    headers: { "Content-Type": "application/json" },
  });
  writeLatency.add(Date.now() - start);

  const ok = check(res, {
    "write 200": (r) => r.status === 200,
  });
  if (!ok) writeErrors.add(1);
}

export function readUrl(data) {
  const codes = data?.codes?.length ? data.codes : ["seed-0"];
  const code  = codes[Math.floor(Math.random() * codes.length)];

  const start = Date.now();
  const res = http.get(`${BASE_URL}/${code}`, { redirects: 0 });
  const elapsed = Date.now() - start;
  readLatency.add(elapsed);

  // 50ms 미만 = Redis 캐시 히트로 간주
  cacheHitRate.add(elapsed < 50);

  const ok = check(res, {
    "read 302 or 400": (r) => r.status === 302 || r.status === 400,
  });
  if (!ok) readErrors.add(1);
}
