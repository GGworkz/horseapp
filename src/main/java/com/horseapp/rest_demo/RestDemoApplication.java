package com.horseapp.rest_demo;

import java.util.NoSuchElementException;
import com.horseapp.rest_demo.Person;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@Bean
  CommandLineRunner runner(PersonRepository repository) {
    return args -> {

      Person person = new Person();
      person.setName("Test123");

      repository.save(person);
      Person saved = repository.findById(person.getId()).orElseThrow(NoSuchElementException::new);
    };
  }


}
