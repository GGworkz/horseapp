package com.horseapp.rest_demo;

import java.time.Duration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/veterinarian")
public class VeterinarianController {

    private final CommonCrudService<Veterinarian, Long> commonCrudService;
    private final Bucket getBucket;
    private final Bucket postBucket;
    private final Bucket deleteBucket;
    private final Bucket putBucket;

    public VeterinarianController(JpaRepository<Veterinarian, Long> repository) {
        this.commonCrudService = new CommonCrudService<>(repository);
        this.getBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(30, Refill.greedy(30, Duration.ofMinutes(1))))
                .build();
        this.putBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))))
                .build();
        this.postBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))))
                .build();
        this.deleteBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1))))
                .build();
    }

    @PostMapping
    public String createVeterinarian(@RequestBody Veterinarian veterinarian) {
        if (postBucket.tryConsume(1)) {
            veterinarian.setId(null);
            Veterinarian createdVeterinarian = commonCrudService.create(veterinarian);
            return String.format("Veterinarian created with ID: %d, Username: %s, Email: %s, Phone: %s",
                    createdVeterinarian.getId(),
                    createdVeterinarian.getUsername(),
                    createdVeterinarian.getEmail(),
                    createdVeterinarian.getPhone());
        } else {
            return "Too many requests";
        }
    }

    @GetMapping("/{id}")
    public String getVeterinarianById(@PathVariable Long id) {
        if (getBucket.tryConsume(1)) {
            return commonCrudService.getById(id)
                    .map(veterinarian -> String.format(
                            "Veterinarian found with ID: %d, Username: %s, Email: %s, Phone: %s",
                            veterinarian.getId(),
                            veterinarian.getUsername(),
                            veterinarian.getEmail(),
                            veterinarian.getPhone()))
                    .orElse("Veterinarian not found!");
        } else {
            return "Too many requests";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteVeterinarian(@PathVariable Long id) {
        if (deleteBucket.tryConsume(1)) {
            if (commonCrudService.delete(id)) {
                return String.format("Veterinarian with ID: %d has been deleted", id);
            } else {
                return String.format("Did not find Veterinarian with ID: %d", id);
            }
        } else {
            return "Too many requests";
        }
    }

    @PutMapping("/{id}")
    public String updateVeterinarian(@PathVariable Long id, @RequestBody Veterinarian veterinarian) {
        if (putBucket.tryConsume(1)) {
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
        } else {
            return "Too many requests";
        }
    }
}
