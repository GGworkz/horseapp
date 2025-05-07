package com.horseapp.service;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import com.horseapp.dto.HorseCreateDTO;
import com.horseapp.dto.HorseUpdateDTO;
import com.horseapp.model.Customer;
import com.horseapp.repository.CustomerRepository;
import com.horseapp.model.Horse;
import com.horseapp.repository.HorseRepository;

import org.springframework.stereotype.Service;

@Service
public class HorseService {

    private final HorseRepository horseRepository;
    private final CustomerRepository customerRepository;

    public HorseService(HorseRepository horseRepository, CustomerRepository customerRepository) {
        this.horseRepository = horseRepository;
        this.customerRepository = customerRepository;
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

    public Horse createHorse(Long customerId, HorseCreateDTO dto) {
        Horse horse = new Horse();
        horse.setName(dto.getName());
        horse.setAge(dto.getAge());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        horse.setCustomer(customer);

        return horseRepository.save(horse);
    }

    public void updateHorse(Long horseId, Long customerId, HorseUpdateDTO horseDto)
            throws EntityNotFoundException, IllegalAccessException {
        Horse horse = horseRepository.findById(horseId)
                .orElseThrow(() -> new EntityNotFoundException("Horse not found"));

        if (!horse.getCustomer().getId().equals(customerId)) {
            throw new IllegalAccessException("Access denied");
        }

        horse.setName(horseDto.getName());
        horse.setAge(horseDto.getAge());

        if (horseDto.getNewOwnerId() != null && !horseDto.getNewOwnerId().equals(customerId)) {
            Customer newOwner = customerRepository.findById(horseDto.getNewOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("New owner not found"));
            horse.setCustomer(newOwner);
        }

        horseRepository.save(horse);
    }

    public void deleteHorse(Long id) {
        horseRepository.deleteById(id);
    }
}
