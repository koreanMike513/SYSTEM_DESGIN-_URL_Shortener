package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.service.strategy.existence.ShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.generator.DeterministicCodeGenerator;

public class StrictShortCodeResolveStrategy extends AbstractShortCodeResolveStrategy {

    private final DeterministicCodeGenerator codeGenerator;
    private final ShortCodeExistenceChecker existenceChecker;

    public StrictShortCodeResolveStrategy(
            DeterministicCodeGenerator codeGenerator,
            ShortCodeExistenceChecker existenceChecker) {
        this.codeGenerator = codeGenerator;
        this.existenceChecker = existenceChecker;
    }

    @Override
    protected String generateAutoCode(String url) {
        return codeGenerator.generate(url);
    }

    // 커스텀 alias가 이미 사용 중이면 예외 → 사용자에게 명확한 피드백
    @Override
    protected String resolveCustomAlias(String alias) {
        if (existenceChecker.exists(alias)) {
            throw new IllegalStateException("이미 사용 중인 단축 URL 입니다: " + alias);
        }
        return alias;
    }
}
