package com.horseapp.controller;

import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.model.Horse;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerUserService;
import com.horseapp.service.HorseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Horse", description = "Horse management")
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

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @GetMapping
    public ResponseEntity<?> getHorses(@PathVariable Long customerId) {
        return ResponseEntity.ok(horseService.getHorsesForCustomer(customerId));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PostMapping
    public ResponseEntity<?> createHorse(@PathVariable Long customerId, @RequestBody Horse horse) {
        horse.getCustomer().setId(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(horseService.createHorse(horse));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PutMapping("/{horseId}")
    public ResponseEntity<?> updateHorse(@PathVariable Long customerId,
                                         @PathVariable Long horseId,
                                         @RequestBody Horse incomingHorse) {
        Optional<Horse> optional = horseService.getHorseById(horseId);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Horse not found");
        }

        Horse existing = optional.get();
        existing.setName(incomingHorse.getName());

        // Horse transfer
        if (incomingHorse.getCustomer() != null && incomingHorse.getCustomer().getId() != null) {
            existing.getCustomer().setId(incomingHorse.getCustomer().getId());
        }

        return ResponseEntity.ok(horseService.updateHorse(existing));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @DeleteMapping("/{horseId}")
    public ResponseEntity<?> deleteHorse(@PathVariable Long customerId, @PathVariable Long horseId) {
        if (!horseService.getHorseById(horseId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Horse not found");
        }
        horseService.deleteHorse(horseId);
        return ResponseEntity.ok("Horse deleted");
    }
}
