package com.horseapp.rest_demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veterinarian")
public class VeterinarianController {

    private final CommonCrudService<Veterinarian, Long> commonCrudService;
    public VeterinarianController(JpaRepository<Veterinarian, Long> repository) {
        this.commonCrudService = new CommonCrudService<>(repository);
    }

    @PostMapping
    public String createVeterinarian(@RequestBody Veterinarian veterinarian) {
        veterinarian.setId(null);
        Veterinarian createdVeterinarian = commonCrudService.create(veterinarian);
        return String.format("Veterinarian created with ID: %d, Username: %s, Email: %s, Phone: %s",
                createdVeterinarian.getId(),
                createdVeterinarian.getUsername(),
                createdVeterinarian.getEmail(),
                createdVeterinarian.getPhone());
    }

    @GetMapping("/{id}")
    public String getVeterinarianById(@PathVariable Long id) {
        return commonCrudService.getById(id)
                .map(veterinarian -> String.format("Veterinarian found with ID: %d, Username: %s, Email: %s, Phone: %s",
                        veterinarian.getId(),
                        veterinarian.getUsername(),
                        veterinarian.getEmail(),
                        veterinarian.getPhone()))
                .orElse("Veterinarian not found!");
    }

    @DeleteMapping("/{id}")
    public String deleteVeterinarian(@PathVariable Long id) {
        if (commonCrudService.delete(id)) {
            return String.format("Veterinarian with ID: %d has been deleted", id);
        } else {
            return String.format("Did not find Veterinarian with ID: %d", id);
        }
    }

    @PutMapping("/{id}")
    public String updateVeterinarian(@PathVariable Long id, @RequestBody Veterinarian veterinarian) {
        return commonCrudService.getById(id)
                .map(existingVeterinarian -> {
                    existingVeterinarian.setUsername(veterinarian.getUsername());
                    existingVeterinarian.setPassword(veterinarian.getPassword());
                    existingVeterinarian.setEmail(veterinarian.getEmail());
                    existingVeterinarian.setPhone(veterinarian.getPhone());
                    commonCrudService.update(existingVeterinarian);
                    return String.format("Veterinarian with ID: %d has been updated", id);
                })
                .orElse("Veterinarian not found!");
    }
}
