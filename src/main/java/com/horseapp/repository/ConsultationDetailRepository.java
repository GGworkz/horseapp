package com.horseapp.repository;

import com.horseapp.model.ConsultationDetail;
import com.horseapp.model.ConsultationDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationDetailRepository extends JpaRepository<ConsultationDetail, ConsultationDetailId> {
    List<ConsultationDetail> findByConsultationId(Long consultationId);
}