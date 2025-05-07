package com.horseapp.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

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

    public ConsultationDetail createDetail(ConsultationDetail detail, Long horseId, Long consultationId) {
        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found"));

        Horse horse = horseRepo.findById(horseId)
                .orElseThrow(() -> new EntityNotFoundException("Horse not found"));

        Long userId = authService.getLoggedInId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Long productId = detail.getProduct().getId();
        ProductCatalog product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        detail.setConsultation(consultation);
        detail.setHorse(horse);
        detail.setUser(user);
        detail.setProduct(product);

        return detailRepo.save(detail);
    }

    public ConsultationDetail update(ConsultationDetail detail) {
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
