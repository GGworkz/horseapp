package com.horseapp.controller;

import com.horseapp.model.Consultation;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.ConsultationService;
import com.horseapp.service.HorseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Consultations", description = "CRUD for consultations per horse")
@RestController
@RequestMapping("/customer/{customerId}/horses/{horseId}/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final AuthorizationService authorizationService;
    private final HorseService horseService;

    public ConsultationController(ConsultationService consultationService,
                                  AuthorizationService authorizationService,
                                  HorseService horseService) {
        this.consultationService = consultationService;
        this.authorizationService = authorizationService;
        this.horseService = horseService;
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @GetMapping
    public ResponseEntity<?> getConsultations(@PathVariable Long customerId,
                                              @PathVariable Long horseId) {
        return ResponseEntity.ok(consultationService.getConsultationsForHorse(horseId));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PostMapping
    public ResponseEntity<?> createConsultation(@PathVariable Long customerId,
                                                @PathVariable Long horseId,
                                                @RequestBody Consultation consultation) {
        try {
            consultation.setHorse(horseService.getHorseByIdOrThrow(horseId));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(consultationService.create(consultation));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PutMapping("/{consultationId}")
    public ResponseEntity<?> updateConsultation(@PathVariable Long customerId,
                                                @PathVariable Long horseId,
                                                @PathVariable Long consultationId,
                                                @RequestBody Consultation consultation) {
        Optional<Consultation> existing = consultationService.getById(consultationId);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consultation not found");
        }

        try {
            consultation.setId(consultationId);
            consultation.setHorse(horseService.getHorseByIdOrThrow(horseId));
            return ResponseEntity.ok(consultationService.update(consultation));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @DeleteMapping("/{consultationId}")
    public ResponseEntity<?> deleteConsultation(@PathVariable Long customerId,
                                                @PathVariable Long consultationId) {
        consultationService.delete(consultationId);
        return ResponseEntity.ok("Consultation deleted");
    }
}
