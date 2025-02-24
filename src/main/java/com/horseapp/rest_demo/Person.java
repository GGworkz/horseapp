package com.horseapp.rest_demo;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
  @SequenceGenerator(name = "person_seq", sequenceName = "person_seq", allocationSize = 1)
  private Long id;
  private String name;

  // getters and setters omitted for brevity
}

interface PersonRepository extends Repository<Person, Long> {

  Person save(Person person);

  Optional<Person> findById(long id);
}