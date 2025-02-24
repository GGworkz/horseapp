package com.horseapp.rest_demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person-belongings")
public class PersonBelongingsController {

    private final CommonCrudService<PersonBelongings, Long> commonCrudService;

    public PersonBelongingsController(@Qualifier("personBelongingsRepository") JpaRepository<PersonBelongings, Long> repository) {
        this.commonCrudService = new CommonCrudService<>(repository);
    }

    @PostMapping
    public String createBelonging(@RequestBody PersonBelongings belongings) {
        belongings.setId(null);
        PersonBelongings createdBelonging = commonCrudService.create(belongings);
        return String.format("Belonging created with ID: %d, Person ID: %d, Item Name: %s",
                createdBelonging.getId(), createdBelonging.getPersonId(), createdBelonging.getItemName());
    }

    @GetMapping("/{id}")
    public String getBelongingById(@PathVariable Long id) {
        return commonCrudService.getById(id)
                .map(belonging -> String.format("Belonging found with ID: %d, Person ID: %d, Item Name: %s",
                        belonging.getId(), belonging.getPersonId(), belonging.getItemName()))
                .orElse("Belonging not found!");
    }

    @DeleteMapping("/{id}")
    public String deleteBelonging(@PathVariable Long id) {
        if (commonCrudService.delete(id)) {
            return String.format("Belonging with ID: %d has been deleted", id);
        } else {
            return String.format("Did not find belonging with ID: %d", id);
        }
        
    }

    @PutMapping("/{id}")
    public String updateBelonging(@PathVariable Long id, @RequestBody PersonBelongings belongings) {
        return commonCrudService.getById(id)
                .map(existingBelonging -> {
                    existingBelonging.setPersonId(belongings.getPersonId());
                    existingBelonging.setItemName(belongings.getItemName());
                    commonCrudService.update(existingBelonging);
                    return String.format("Belonging with ID: %d has been updated", id);
                })
                .orElse("Belonging not found!");
    }
}
