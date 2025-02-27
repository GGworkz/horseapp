package com.horseapp.controller;

import com.horseapp.model.Veterinarian;
import com.horseapp.service.VeterinarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VeterinarianController {
    private final VeterinarianService veterinarianService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @PostMapping("/veterinarian")
    public void createVeterinarian(@RequestBody Veterinarian veterinarian) {
        System.out.println("In controller:" + veterinarian);
        veterinarianService.create(veterinarian);
    }

    @GetMapping("/veterinarian/{id}")
    public Veterinarian getVeterinarianById(@PathVariable long id) {
        return veterinarianService.findById(id);
    }
}
