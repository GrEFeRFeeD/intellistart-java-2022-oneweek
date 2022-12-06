package com.intellias.intellistart.interviewplanning.config;

import java.time.Duration;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * OK.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class RedisConfig {

  private String host;
  private String password;

  /**
   * OK.
   */
  @Bean
  @Primary
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(
          RedisConfiguration defaultRedisConfig) {
    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
              .useSsl().and()
              .commandTimeout(Duration.ofMillis(60000)).build();
    return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
  }

  /**
   * OK.
   */
  @Bean
  public RedisConfiguration defaultRedisConfig() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName("redis");
    config.setPassword(RedisPassword.of(password));
    return config;
  }
}