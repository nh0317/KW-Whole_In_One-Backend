package com.naturemobility.seoul.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void setValues(String token, String email){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, email);
    }

    public String getValuse(String token){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    public Boolean isExist(String token){
        return redisTemplate.hasKey(token);
    }
    public void deleteValues(String token){
        redisTemplate.delete(token.substring(7));
    }
    public void setValueExpire(String token, String email, long duration){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        values.set(token, email, expireDuration);
    }
}
