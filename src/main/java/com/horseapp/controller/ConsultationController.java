package com.horseapp.controller;

import java.time.ZonedDateTime;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.dto.ConsultationCreateDTO;
import com.horseapp.dto.ConsultationUpdateDTO;
import com.horseapp.model.Consultation;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.ConsultationService;
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

@Tag(name = "Consultations", description = "Consultations per horse management")
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
                                                @RequestBody ConsultationCreateDTO request) {
        try {
            Consultation consultation = new Consultation();
            consultation.setTimestamp(request.getTimestamp());
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
                                                @RequestBody ConsultationUpdateDTO updates) {
        Optional<Consultation> optional = consultationService.getById(consultationId);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consultation not found");
        }

        try {
            Consultation consultation = optional.get();
            ZonedDateTime effectiveTimestamp =
                    updates.getTimestamp() != null ? updates.getTimestamp() : ZonedDateTime.now();

            consultation.setTimestamp(effectiveTimestamp);
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
