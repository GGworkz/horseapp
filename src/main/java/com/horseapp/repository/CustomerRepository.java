package com.horseapp.repository;

import java.util.Optional;
import java.util.Set;

import com.horseapp.model.Customer;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
    Set<Customer> findByUsersId(Long userId);
}
