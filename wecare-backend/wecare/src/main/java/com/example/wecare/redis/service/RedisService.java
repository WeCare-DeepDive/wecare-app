package com.example.wecare.redis.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void setValues(String key, String value, long timeout, TimeUnit unit);
    String getValues(String key);
    void deleteValues(String key);
}
