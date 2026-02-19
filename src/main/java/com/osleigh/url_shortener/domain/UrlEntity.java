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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "urls")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = PROTECTED)
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
  private LocalDateTime expiresAt;

  public static UrlEntity create(UrlCreateRequest param) {
    UrlEntity entity = new UrlEntity();
    entity.originalURL = param.originalURL();
    entity.shortCode = param.shortCode();
    entity.isCustom = param.isCustom();
    entity.expiresAt = param.expiresAt() != null
        ? param.expiresAt()
        : LocalDateTime.now().plusDays(30);
    return entity;
  }

  public boolean isExpired() {
    return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
  }

  public Boolean isCustomUrl() {
    return isCustom;
  }
}
