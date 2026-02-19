package com.osleigh.url_shortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@SpringBootTest
@ActiveProfiles("test")
class UrlEntityShortenerApplicationTests {

	@MockitoBean
	LettuceConnectionFactory redisConnectionFactory;

	@Test
	void contextLoads() {
	}

}
