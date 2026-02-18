package com.osleigh.url_shortener.repository;

import com.osleigh.url_shortener.domain.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRedirectRepository extends JpaRepository<UrlEntity, Long>  {

  Optional<UrlEntity> findByShortCode(String shortCode);
}
