package com.osleigh.url_shortener.code;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlEntity;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;

public class URLEntityGenerator {

  public static UrlEntity createUrlEntity(
      Long id,
      String originalUrl,
      String shortCode,
      boolean isCustom
  ) {
    try {
      Constructor<UrlEntity> constructor = UrlEntity.class.getDeclaredConstructor(
          Long.class, URL.class, String.class, boolean.class, Timestamp.class
      );
      constructor.setAccessible(true);
      return constructor.newInstance(id, new URL(originalUrl), shortCode, isCustom, null);
    } catch (Exception e) {
      throw new RuntimeException("UrlEntity 생성 실패", e);
    }
  }
}
