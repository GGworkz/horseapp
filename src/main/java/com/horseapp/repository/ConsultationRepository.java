package com.horseapp.repository;

import java.util.List;

import com.horseapp.model.Consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByHorseId(Long horseId);
}
