package com.osleigh.url_shortener.repository;

import com.osleigh.url_shortener.domain.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlShortenerRepository extends JpaRepository<UrlEntity, Long>  {

  boolean existsByShortCode(String shortCode);
}
