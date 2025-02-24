package com.horseapp.rest_demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestDemoApplication.class, args);
    }

//    // If you want to run some initialization logic, you can still keep this CommandLineRunner.
//    @Bean
//    CommandLineRunner runner(PersonRepository repository) {
//        return args -> {
//            // You can add any initialization logic here if needed
//        };
//    }
//
//    @Bean
//    CommandLineRunner runner(PersonBelongingsRepository repository) {
//        return args -> {
//            // You can add any initialization logic here if needed
//        };
//    }
}
