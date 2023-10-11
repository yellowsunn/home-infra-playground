package com.yellowsunn.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ExampleController {

    private final String uuid;

    public ExampleController() {
        this.uuid = UUID.randomUUID().toString();
    }

    @GetMapping
    public String hello() {
        return "hello world: " + uuid;
    }
}
