package com.horseapp.rest_demo;

import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@Entity
@Table(name = "veterinarian")
class Veterinarian {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "veterinarian_seq")
  @SequenceGenerator(name = "veterinarian_seq", sequenceName = "veterinarian_seq", allocationSize = 1)
  private Long id;
  private String username;
  private String password;
  private String email;
  private String phone;
}

@Repository
interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
}
