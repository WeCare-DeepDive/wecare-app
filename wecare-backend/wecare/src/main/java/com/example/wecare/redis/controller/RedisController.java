package com.example.wecare.redis.controller;

import com.example.wecare.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @PostMapping("/set")
    public ResponseEntity<String> setKey(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam(defaultValue = "60") long ttlSeconds) {
                redisService.setValues(key, value, ttlSeconds, TimeUnit.MILLISECONDS);
        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getKey( @RequestParam String key) {
        String value = redisService.getValues(key);
        if (value == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않음");
        }
        return ResponseEntity.ok(value);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteKey( @RequestParam String key) {
        redisService.deleteValues(key);
        return ResponseEntity.ok("삭제 완료");
    }
}
