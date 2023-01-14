package com.example.firstservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/first-service")
public class FirstServiceController {
    private final Environment environment; // yml 설정 파일 가져오는 클래스

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First Service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String firstRequestHeader) {
        log.info(firstRequestHeader);
        return "Hello World in First Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest req) {
        log.info("Server Port={}", req.getServerPort());
        return String.format("Hi, there. This is a message from First Service on PORT %s",
                environment.getProperty("local.server.port"));
    }
}
