package com.osleigh.url_shortener.config.properties;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 *   URL 값 객체를 만든 건 사용자 입력이 들어오는 도메인 경계라서 검증이 의미 있었던 거고, 설정값은 개발자가 직접 넣는 값이라 성격이 다릅니다.
 *   값 객체로 감싸는 게 의미 있는 경우는 그 값을 가지고 도메인 로직을 수행할 때인데, host는 단순 문자열 결합(host + "/" + code)만 하니까 String이면 충분합니다.
 */
@Valid
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class DomainProperties {

  @NotBlank
  String host;
}
