package com.horseapp.service;

import com.horseapp.model.Veterinarian;
import com.horseapp.repository.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VeterinarianService {
    private final VeterinarianRepository veterinarianRepository;

    @Autowired
    public VeterinarianService(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    public void create(Veterinarian veterinarian) {
        veterinarianRepository.save(veterinarian);
    }

    public Veterinarian findById(long id) {
        return veterinarianRepository.findById(id).get();
    }
}
