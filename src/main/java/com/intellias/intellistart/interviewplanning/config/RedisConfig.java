package com.intellias.intellistart.interviewplanning.config;

import com.intellias.intellistart.interviewplanning.controllers.dto.JwtRequest;
import com.intellias.intellistart.interviewplanning.controllers.dto.JwtResponse;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

@Component
public class RedisConfig {

    public boolean checkIfExist(JwtRequest jwtRequest){
      JedisPooled jedis = new JedisPooled("localhost", 6379);

      return jedis.get(String.valueOf(jwtRequest)) != null;
    }

    public JwtResponse getAndResponse(JwtRequest jwtRequest){
        JedisPooled jedis = new JedisPooled("localhost", 6379);

        return new JwtResponse(jedis.get(String.valueOf(jwtRequest)));
    }


}
