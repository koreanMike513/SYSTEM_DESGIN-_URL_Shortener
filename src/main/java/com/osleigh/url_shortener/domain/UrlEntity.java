package com.osleigh.url_shortener.domain;


import com.osleigh.url_shortener.domain.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "urls")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access =  PROTECTED)
public class UrlEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @Embedded
  @AttributeOverride(name = "url", column = @Column(name = "original_url", nullable = false))
  private URL originalURL;

  @Column(name = "short_code", unique = true, nullable = false)
  private String shortCode;

  @Column(name = "is_custom", nullable = false)
  private boolean isCustom;

  @Column(name = "expires_at")
  private Timestamp expiresAt;

  public static UrlEntity create(UrlEntityCreateParam param) {
    UrlEntity entity = new UrlEntity();
    entity.originalURL = param.originalURL();
    entity.shortCode = param.shortCode();
    entity.isCustom = param.isCustom();
    entity.expiresAt = param.expiresAt();
    return entity;
  }

  public Boolean isCustomUrl() {
    return isCustom;
  }
}
