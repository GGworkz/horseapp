package com.horseapp.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.model.ConsultationDetail;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.ConsultationDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Consultation Details", description = "Consultation itemized per product management")
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

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateDetail(@PathVariable Long customerId,
                                          @PathVariable Long consultationId,
                                          @PathVariable Long productId,
                                          @RequestBody ConsultationDetail incomingDetail) {
        try {
            ConsultationDetail existing = detailService.getDetail(consultationId, productId);
            existing.setQuantity(incomingDetail.getQuantity());
            return ResponseEntity.ok(detailService.update(existing));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
