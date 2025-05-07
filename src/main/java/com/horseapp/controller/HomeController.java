package com.horseapp.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "Check status of application")
@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return Map.of("status", "running").toString();
    }
}