package com.horseapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="vet")
public class Veterinarian {
    @Id
    private Long id;
    private String name;
}
