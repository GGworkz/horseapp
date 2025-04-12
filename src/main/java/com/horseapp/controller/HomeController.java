package com.horseapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "Landing Page Handler")
@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Spring Boot up and running!";
    }
}