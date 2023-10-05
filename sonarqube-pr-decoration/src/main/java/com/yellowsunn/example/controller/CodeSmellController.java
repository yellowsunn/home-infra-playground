package com.yellowsunn.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CodeSmellController {
    @GetMapping("/")
    public List<? extends String> smell() {
        String nullString = null;
        if (nullString.equals("hello")) {
            throw new RuntimeException("");
        }

        return List.of("hello1", "hello2");
    }
}
