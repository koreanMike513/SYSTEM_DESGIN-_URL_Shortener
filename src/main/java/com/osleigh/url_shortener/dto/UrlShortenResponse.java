package com.osleigh.url_shortener.dto;

import com.osleigh.url_shortener.domain.UrlEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenResponse {

  private Long id;

  private String originalURL;

  private String redirectURL;

  private boolean isCustom;

  private Timestamp createdAt;

  private Timestamp updatedAt;

  private Timestamp expiresAt;

  public static UrlShortenResponse fromEntity(UrlEntity entity, String host) {
    return new UrlShortenResponse(
        entity.getId(),
        entity.getOriginalURL().url(),
        host + "/" + entity.getShortCode(),
        entity.isCustomUrl(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getExpiresAt()
    );
  }
}
