package com.horseapp.repository;

import com.horseapp.model.Horse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
    List<Horse> findByCustomerId(Long customerId);
}
