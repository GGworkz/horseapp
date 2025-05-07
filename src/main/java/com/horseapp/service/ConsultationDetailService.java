package com.horseapp.service;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import com.horseapp.dto.ConsultationTotalDTO;
import com.horseapp.dto.ConsultationDetailCreateDTO;
import com.horseapp.dto.ConsultationDetailResponseDTO;
import com.horseapp.dto.ConsultationDetailUpdateDTO;
import com.horseapp.model.Consultation;
import com.horseapp.model.ConsultationDetail;
import com.horseapp.model.ConsultationDetailId;
import com.horseapp.model.Horse;
import com.horseapp.model.ProductCatalog;
import com.horseapp.model.User;

import com.horseapp.repository.ConsultationDetailRepository;
import com.horseapp.repository.ConsultationRepository;
import com.horseapp.repository.HorseRepository;
import com.horseapp.repository.ProductCatalogRepository;
import com.horseapp.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class ConsultationDetailService {

    private final ConsultationDetailRepository detailRepo;
    private final ConsultationRepository consultationRepo;
    private final HorseRepository horseRepo;
    private final ProductCatalogRepository productRepo;
    private final UserRepository userRepo;
    private final AuthorizationService authService;

    public ConsultationDetailService(ConsultationDetailRepository detailRepo,
                                     ConsultationRepository consultationRepo,
                                     HorseRepository horseRepo,
                                     ProductCatalogRepository productRepo,
                                     UserRepository userRepo,
                                     AuthorizationService authService) {
        this.detailRepo = detailRepo;
        this.consultationRepo = consultationRepo;
        this.horseRepo = horseRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.authService = authService;
    }

    public List<ConsultationDetail> findByConsultationId(Long consultationId) {
        return detailRepo.findByConsultationId(consultationId);
    }

    public List<ConsultationDetailResponseDTO> findDetailResponsesByConsultationId(Long consultationId) {
        List<ConsultationDetail> details = detailRepo.findByConsultationId(consultationId);

        return details.stream().map(detail -> {
            ConsultationDetailResponseDTO dto = new ConsultationDetailResponseDTO();
            dto.setConsultationId(detail.getConsultation().getId());
            dto.setProductId(detail.getProduct().getId());
            dto.setProductName(detail.getProduct().getName());
            dto.setProductPrice(detail.getProduct().getPrice());
            dto.setProductType(detail.getProduct().getType());
            dto.setQuantity(detail.getQuantity());
            return dto;
        }).toList();
    }

    public ConsultationTotalDTO getTotalPriceByConsultationId(Long consultationId) {
        List<ConsultationDetail> details = detailRepo.findByConsultationId(consultationId);

        BigDecimal total = details.stream()
                .map(d -> d.getProduct().getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ConsultationTotalDTO dto = new ConsultationTotalDTO();
        dto.setConsultationId(consultationId);
        dto.setTotalPrice(total);
        return dto;
    }

    public ConsultationDetail createDetail(ConsultationDetailCreateDTO dto, Long horseId, Long consultationId) {
        Long userId = authService.getLoggedInId();
        String role = authService.getLoggedInRole();
        if (!"user".equals(role)) {
            throw new IllegalStateException("Only users can add consultation details.");
        }

        ProductCatalog product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        Horse horse = horseRepo.findById(horseId)
                .orElseThrow(() -> new EntityNotFoundException("Horse not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ConsultationDetail detail = new ConsultationDetail();
        detail.setConsultation(consultation);
        detail.setHorse(horse);
        detail.setProduct(product);
        detail.setUser(user);
        detail.setQuantity(dto.getQuantity());

        return detailRepo.save(detail);
    }

    public ConsultationDetail updateDetail(ConsultationDetailUpdateDTO dto,
                                           Long horseId,
                                           Long consultationId,
                                           Long existingProductId) {

        Long userId = authService.getLoggedInId();
        String role = authService.getLoggedInRole();
        if (!"user".equals(role)) {
            throw new IllegalStateException("Only users can update consultation details.");
        }

        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        if (!consultation.getHorse().getId().equals(horseId)) {
            throw new IllegalArgumentException("Consultation does not belong to this horse.");
        }

        ConsultationDetailId id = new ConsultationDetailId(consultationId, existingProductId);
        ConsultationDetail detail = detailRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultation detail not found"));

        detail.setQuantity(dto.getQuantity());

        return detailRepo.save(detail);
    }

    public ConsultationDetail getDetail(Long consultationId, Long productId) {
        return detailRepo.findByConsultationIdAndProductId(consultationId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation detail not found"));
    }

    public void deleteDetail(Long consultationId, Long productId) {
        ConsultationDetailId id = new ConsultationDetailId(consultationId, productId);
        detailRepo.deleteById(id);
    }
}
