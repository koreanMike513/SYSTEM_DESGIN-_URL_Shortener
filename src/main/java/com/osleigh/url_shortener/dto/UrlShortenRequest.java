package com.osleigh.url_shortener.dto;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.domain.UrlCreateRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenRequest {

  @NotBlank(message = "원본 URL은 필수입니다")
  private String originalURL;

  private String alias;

  public boolean isCustomRequest() {
    return alias != null && !alias.isBlank();
  }

  public UrlEntity toEntity(String shortCode) {
    return UrlEntity.create(new UrlCreateRequest(new URL(originalURL), shortCode, isCustomRequest(), null));
  }
}
