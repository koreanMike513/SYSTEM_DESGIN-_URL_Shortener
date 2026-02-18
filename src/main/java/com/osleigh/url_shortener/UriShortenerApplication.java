package com.osleigh.url_shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class UriShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UriShortenerApplication.class, args);
	}

}
