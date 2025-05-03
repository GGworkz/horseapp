package com.horseapp.service;

import com.horseapp.model.Horse;
import com.horseapp.repository.HorseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorseService {

    private final HorseRepository horseRepository;

    public HorseService(HorseRepository horseRepository) {
        this.horseRepository = horseRepository;
    }

    public List<Horse> getHorsesForCustomer(Long customerId) {
        return horseRepository.findByCustomerId(customerId);
    }

    public Optional<Horse> getHorseById(Long id) {
        return horseRepository.findById(id);
    }

    public Horse getHorseByIdOrThrow(Long id) {
        return horseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horse not found"));
    }

    public Horse createHorse(Horse horse) {
        return horseRepository.save(horse);
    }

    public Horse updateHorse(Horse horse) {
        return horseRepository.save(horse);
    }

    public void deleteHorse(Long id) {
        horseRepository.deleteById(id);
    }
}
