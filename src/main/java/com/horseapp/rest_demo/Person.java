package com.horseapp.rest_demo;

import java.util.Optional;
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
@Table(name = "person")
class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_seq")
  @SequenceGenerator(name = "person_id_seq", sequenceName = "person_id_seq", allocationSize = 1)
  private Long id;
  private String name;
  private int age;
}

@Repository
interface PersonRepository extends JpaRepository<Person, Long> {
}