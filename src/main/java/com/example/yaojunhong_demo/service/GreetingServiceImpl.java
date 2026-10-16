package com.example.yaojunhong_demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GreetingServiceImpl implements GreetingService {

    @Override
    public Map<String, Object> greet(String name) {
        return Map.of(
                "message", "Hello, " + name + "!",
                "timestamp", LocalDateTime.now()
        );
    }

    @Override
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "Yaojunhong_demo",
                "timestamp", LocalDateTime.now()
        );
    }
}
