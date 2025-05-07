package com.horseapp.repository;

import java.util.List;
import java.util.Optional;

import com.horseapp.model.ConsultationDetail;
import com.horseapp.model.ConsultationDetailId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationDetailRepository extends JpaRepository<ConsultationDetail, ConsultationDetailId> {
    List<ConsultationDetail> findByConsultationId(Long consultationId);
    Optional<ConsultationDetail> findByConsultationIdAndProductId(Long consultationId, Long productId);
}