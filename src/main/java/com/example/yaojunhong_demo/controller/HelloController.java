package com.example.yaojunhong_demo.controller;

import com.example.yaojunhong_demo.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelloController {

    private final GreetingService greetingService;

    @GetMapping("/hello")
    public Map<String, Object> hello(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        return greetingService.greet(name);
    }

    @GetMapping("/hello/{name}")
    public Map<String, Object> helloWithPath(@PathVariable String name) {
        return greetingService.greet(name);
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return greetingService.health();
    }
}
