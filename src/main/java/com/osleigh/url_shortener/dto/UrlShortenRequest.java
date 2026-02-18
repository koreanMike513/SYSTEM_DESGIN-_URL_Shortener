package com.osleigh.url_shortener.dto;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.domain.UrlEntityCreateParam;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
public class UrlShortenRequest {

  @NotBlank(message = "원본 URL은 필수입니다")
  private String originalURL;

  private String alias;

  public Boolean isCustomRequest() {
    return alias != null;
  }

  public UrlEntity toEntity(String shortCode) {
    return UrlEntity.create(new UrlEntityCreateParam(new URL(originalURL), shortCode, isCustomRequest(), null));
  }
}
