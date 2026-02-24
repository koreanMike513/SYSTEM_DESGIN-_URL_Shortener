package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 단일 저장 시도의 원자적 트랜잭션 단위
// REQUIRES_NEW: 충돌 시 이 트랜잭션만 롤백 → Orchestrator에서 새 트랜잭션으로 재시도 가능
@Component
@RequiredArgsConstructor
public class UrlWriter {

    private final UrlRepository urlRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(UrlEntity urlEntity) {
        urlRepository.save(urlEntity);
    }
}
