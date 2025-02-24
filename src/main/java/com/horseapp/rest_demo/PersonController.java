package com.horseapp.rest_demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final CommonCrudService<Person, Long> commonCrudService;

    public PersonController(@Qualifier("personRepository") JpaRepository<Person, Long> repository) {
        this.commonCrudService = new CommonCrudService<>(repository);
    }

    @PostMapping
    public String createPerson(@RequestBody Person person) {
        person.setId(null);
        Person createdPerson = commonCrudService.create(person);
        return String.format("Person created with ID: %d, Name: %s, Age: %d",
                createdPerson.getId(), createdPerson.getName(), createdPerson.getAge());
    }

    @GetMapping("/{id}")
    public String getPersonById(@PathVariable Long id) {
        return commonCrudService.getById(id)
                .map(person -> String.format("Person found with ID: %d, Name: %s, Age: %d",
                        person.getId(), person.getName(), person.getAge()))
                .orElse("Person not found!");
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable Long id) {
        commonCrudService.delete(id);
        return String.format("Person with ID: %d has been deleted", id);
    }

    @PutMapping("/{id}")
    public String updatePerson(@PathVariable Long id, @RequestBody Person person) {
        return commonCrudService.getById(id)
                .map(existingPerson -> {
                    existingPerson.setName(person.getName());
                    existingPerson.setAge(person.getAge());
                    commonCrudService.update(existingPerson);
                    return String.format("Person with ID: %d has been updated", id);
                })
                .orElse("Person not found!");
    }
}
