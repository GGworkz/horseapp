package com.horseapp.service;

import java.util.List;
import java.util.Optional;

import com.horseapp.model.Consultation;
import com.horseapp.repository.ConsultationRepository;

import org.springframework.stereotype.Service;


@Service
public class ConsultationService {

    private final ConsultationRepository repository;

    public ConsultationService(ConsultationRepository repository) {
        this.repository = repository;
    }

    public List<Consultation> getConsultationsForHorse(Long horseId) {
        return repository.findByHorseId(horseId);
    }

    public Optional<Consultation> getById(Long id) {
        return repository.findById(id);
    }

    public Consultation create(Consultation consultation) {
        return repository.save(consultation);
    }

    public Consultation update(Consultation consultation) {
        return repository.save(consultation);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
