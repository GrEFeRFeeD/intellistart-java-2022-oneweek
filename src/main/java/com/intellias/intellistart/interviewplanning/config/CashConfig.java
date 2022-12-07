package com.intellias.intellistart.interviewplanning.config;

import com.google.common.cache.CacheBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Cach configuration.
 */
@Configuration
@EnableCaching
public class CashConfig extends CachingConfigurerSupport {
  @Value("${jwt.validity}")
  private Long jwtValidity;

  @Bean
  @Override
  public CacheManager cacheManager() {
    ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {

      @Override
      protected Cache createConcurrentMapCache(final String name) {
        return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(
                        jwtValidity, TimeUnit.SECONDS)
                .maximumSize(100).build().asMap(), false);
      }
    };

    cacheManager.setCacheNames(List.of("user", "jwt"));
    return cacheManager;
  }

}