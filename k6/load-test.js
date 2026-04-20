import http from "k6/http";
import { check, sleep } from "k6";
import { Counter, Rate, Trend } from "k6/metrics";

// ──────────────────────────────────────────
// TPS 가정: 쓰기 1,000 / 읽기 9,000 (9:1)
// 단계적 ramping → 목표 TPS 유지 → cooldown
// ──────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

const writeErrors  = new Counter("write_errors");
const readErrors   = new Counter("read_errors");
const writeLatency = new Trend("write_latency_ms", true);
const readLatency  = new Trend("read_latency_ms",  true);
const cacheHitRate = new Rate("cache_hit_rate");

export const options = {
  scenarios: {
    // 쓰기: 1,000 TPS (10% 트래픽)
    write_urls: {
      executor:           "ramping-arrival-rate",
      startRate:          0,
      timeUnit:           "1s",
      preAllocatedVUs:    50,
      maxVUs:             200,
      stages: [
        { target: 200,   duration: "2m"  },   // ramp-up
        { target: 1000,  duration: "3m"  },   // ramp-up to target
        { target: 1000,  duration: "10m" },   // sustained
        { target: 0,     duration: "2m"  },   // cooldown
      ],
      exec: "writeUrl",
    },
    // 읽기: 9,000 TPS (90% 트래픽)
    read_urls: {
      executor:           "ramping-arrival-rate",
      startRate:          0,
      timeUnit:           "1s",
      preAllocatedVUs:    200,
      maxVUs:             1000,
      stages: [
        { target: 1000,  duration: "2m"  },
        { target: 9000,  duration: "3m"  },
        { target: 9000,  duration: "10m" },
        { target: 0,     duration: "2m"  },
      ],
      exec: "readUrl",
    },
  },

  thresholds: {
    // p95 응답시간
    "write_latency_ms": ["p(95)<500"],   // 쓰기 p95 < 500ms
    "read_latency_ms":  ["p(95)<50"],    // 읽기 p95 < 50ms (Redis 캐시)
    // 에러율
    "write_errors":     ["count<100"],
    "read_errors":      ["count<500"],
    // HTTP 에러율 전체 1% 미만
    "http_req_failed":  ["rate<0.01"],
  },
};

// 테스트용 short code 풀 (사전에 seeding 하거나 공유 array 사용)
const SHORT_CODE_POOL = Array.from({ length: 1000 }, (_, i) => `test-code-${i}`);

// ── 쓰기 시나리오 ─────────────────────────
export function writeUrl() {
  const payload = JSON.stringify({
    originalURL: `https://example.com/path/${Math.random().toString(36).slice(2)}`,
  });

  const start = Date.now();
  const res = http.post(`${BASE_URL}/api/v1/urls/shorten`, payload, {
    headers: { "Content-Type": "application/json" },
  });
  writeLatency.add(Date.now() - start);

  const ok = check(res, {
    "write: status 200": (r) => r.status === 200,
    "write: has redirectURL": (r) => {
      try { return JSON.parse(r.body).redirectURL !== undefined; }
      catch { return false; }
    },
  });

  if (!ok) writeErrors.add(1);
}

// ── 읽기 시나리오 ─────────────────────────
export function readUrl() {
  const shortCode = SHORT_CODE_POOL[Math.floor(Math.random() * SHORT_CODE_POOL.length)];

  const start = Date.now();
  // maxRedirects: 0 → 리다이렉트 따라가지 않고 302 응답 자체를 검증
  const res = http.get(`${BASE_URL}/${shortCode}`, { redirects: 0 });
  const elapsed = Date.now() - start;
  readLatency.add(elapsed);

  // 50ms 미만이면 캐시 히트로 간주
  cacheHitRate.add(elapsed < 50);

  const ok = check(res, {
    "read: status 302 or 400": (r) => r.status === 302 || r.status === 400,
    "read: 302 has Location": (r) =>
      r.status !== 302 || r.headers["Location"] !== undefined,
  });

  if (!ok) readErrors.add(1);
}

// ── 테스트 전 seed 데이터 생성 ─────────────
export function setup() {
  console.log(`[setup] Seeding ${SHORT_CODE_POOL.length} short codes...`);
  for (const code of SHORT_CODE_POOL) {
    http.post(`${BASE_URL}/api/v1/urls/shorten`,
      JSON.stringify({ originalURL: "https://example.com/seed", alias: code }),
      { headers: { "Content-Type": "application/json" } }
    );
  }
  console.log("[setup] Seed complete.");
}
