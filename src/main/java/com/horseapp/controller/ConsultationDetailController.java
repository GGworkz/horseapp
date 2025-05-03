package com.horseapp.controller;

import com.horseapp.model.*;
import com.horseapp.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Consultation Details", description = "Link products to consultations")
@RestController
@RequestMapping("/customer/{customerId}/horses/{horseId}/consultations/{consultationId}/details")
public class ConsultationDetailController {

    private final ConsultationDetailService detailService;
    private final AuthorizationService authorizationService;

    public ConsultationDetailController(ConsultationDetailService detailService,
                                        AuthorizationService authorizationService) {
        this.detailService = detailService;
        this.authorizationService = authorizationService;
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @GetMapping
    public ResponseEntity<List<ConsultationDetail>> getDetails(@PathVariable Long consultationId,
                                                               @PathVariable Long customerId) {
        return ResponseEntity.ok(detailService.findByConsultationId(consultationId));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PostMapping
    public ResponseEntity<?> addDetail(@PathVariable Long customerId,
                                       @PathVariable Long horseId,
                                       @PathVariable Long consultationId,
                                       @RequestBody ConsultationDetail detail) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    detailService.createDetail(detail, horseId, consultationId)
            );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteDetail(@PathVariable Long customerId,
                                          @PathVariable Long consultationId,
                                          @PathVariable Long productId) {
        detailService.deleteDetail(consultationId, productId);
        return ResponseEntity.ok("Detail deleted");
    }
}
