package com.horseapp.rest_demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RestDemoApplication {

    private final PersonRepository repository;

    // Constructor injection
    public RestDemoApplication(PersonRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestDemoApplication.class, args);
    }

    @PostMapping("/person")
    public String createPerson(@RequestBody Person person) {
        // Save the person and ensure ID is assigned
        repository.save(person);
        return String.format("Person created with ID: %d and Name: %s", person.getId(), person.getName());
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            Person person = new Person();
            person.setName("Test123");

            // Save the person and ensure ID is assigned
            repository.save(person);
            System.out.println("Saved Person ID: " + person.getId());

            // Find the person safely and log the result
            repository.findById(person.getId())
                    .ifPresentOrElse(
                            found -> System.out.println("Retrieved Person: " + found.getName()),
                            () -> System.out.println("Person not found!")
                    );
        };
    }
}
