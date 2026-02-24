package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.service.strategy.generator.DeterministicCodeGenerator;

public class LenientShortCodeResolveStrategy extends AbstractShortCodeResolveStrategy {

    private final DeterministicCodeGenerator codeGenerator;

    public LenientShortCodeResolveStrategy(DeterministicCodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    protected String generateAutoCode(String url) {
        return codeGenerator.generate(url);
    }

    @Override
    protected String resolveCustomAlias(String alias) {
        return alias;
    }
}
