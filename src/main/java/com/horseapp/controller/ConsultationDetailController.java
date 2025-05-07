package com.horseapp.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.dto.ConsultationDetailCreateDTO;
import com.horseapp.dto.ConsultationDetailUpdateDTO;
import com.horseapp.dto.ConsultationDetailResponseDTO;
import com.horseapp.dto.ConsultationTotalDTO;
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
    public ResponseEntity<List<ConsultationDetailResponseDTO>> getDetails(@PathVariable Long customerId,
                                                                          @PathVariable Long horseId,
                                                                          @PathVariable Long consultationId) {
        return ResponseEntity.ok(detailService.findDetailResponsesByConsultationId(consultationId));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @GetMapping("/total")
    public ResponseEntity<ConsultationTotalDTO> getTotal(@PathVariable Long customerId,
                                                         @PathVariable Long horseId,
                                                         @PathVariable Long consultationId) {
        return ResponseEntity.ok(detailService.getTotalPriceByConsultationId(consultationId));
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @PostMapping
    public ResponseEntity<?> addDetail(@PathVariable Long customerId,
                                       @PathVariable Long horseId,
                                       @PathVariable Long consultationId,
                                       @RequestBody ConsultationDetailCreateDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    detailService.createDetail(dto, horseId, consultationId)
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
                                          @PathVariable Long horseId,
                                          @PathVariable Long consultationId,
                                          @PathVariable Long productId,
                                          @RequestBody ConsultationDetailUpdateDTO dto) {
        try {
            ConsultationDetail updated = detailService.updateDetail(dto, horseId, consultationId, productId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
