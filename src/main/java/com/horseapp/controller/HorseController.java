package com.horseapp.controller;

import com.horseapp.model.Horse;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerUserService;
import com.horseapp.service.HorseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Horse", description = "Horse management APIs")
@RestController
@RequestMapping("/customer/{customerId}/horses")
public class HorseController {

    private final HorseService horseService;
    private final AuthorizationService authorizationService;
    private final CustomerUserService customerUserService;

    public HorseController(HorseService horseService,
                           AuthorizationService authorizationService,
                           CustomerUserService customerUserService) {
        this.horseService = horseService;
        this.authorizationService = authorizationService;
        this.customerUserService = customerUserService;
    }

    private boolean hasAccessToCustomer(Long customerId) {
        String role = authorizationService.getLoggedInRole();
        long id = authorizationService.getLoggedInId();

        return (role.equals("customer") && id == customerId)
                || (role.equals("user") && customerUserService.getCustomers(id).stream()
                .anyMatch(c -> c.getId().equals(customerId)));
    }

    @GetMapping
    public ResponseEntity<?> getHorses(@PathVariable Long customerId) {
        if (!hasAccessToCustomer(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(horseService.getHorsesForCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<?> createHorse(@PathVariable Long customerId, @RequestBody Horse horse) {
        if (!hasAccessToCustomer(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        horse.getCustomer().setId(customerId); // ensure correct ownership
        return ResponseEntity.status(HttpStatus.CREATED).body(horseService.createHorse(horse));
    }

    @PutMapping("/{horseId}")
    public ResponseEntity<?> updateHorse(@PathVariable Long customerId,
                                         @PathVariable Long horseId,
                                         @RequestBody Horse horse) {
        if (!hasAccessToCustomer(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (!horseService.getHorseById(horseId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Horse not found");
        }
        horse.setId(horseId);
        horse.getCustomer().setId(customerId);
        return ResponseEntity.ok(horseService.updateHorse(horse));
    }

    @DeleteMapping("/{horseId}")
    public ResponseEntity<?> deleteHorse(@PathVariable Long customerId, @PathVariable Long horseId) {
        if (!hasAccessToCustomer(customerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (!horseService.getHorseById(horseId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Horse not found");
        }
        horseService.deleteHorse(horseId);
        return ResponseEntity.ok("Horse deleted");
    }
}
