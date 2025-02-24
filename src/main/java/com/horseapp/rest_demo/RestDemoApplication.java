package com.horseapp.rest_demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class RestDemoApplication {

    private final PersonRepository repository;
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

    @GetMapping("/person/{id}")
    public String getPersonById(@PathVariable Long id) {
        // Find person by ID
        Person person = repository.findById(id).orElse(null);
        if (person != null) {
            return String.format("Person found with ID: %d and Name: %s", person.getId(), person.getName());
        } else {
            return "Person not found!";
        }
    }

    @DeleteMapping("/person/{id}")
    public String deletePerson(@PathVariable Long id) {
        // Check if the person exists
        Person person = repository.findById(id).orElse(null);
        if (person != null) {
            repository.delete(person);  // Delete the person from the database
            return String.format("Person with ID: %d has been deleted", id);
        } else {
            return "Person not found!";
        }
    }
}
