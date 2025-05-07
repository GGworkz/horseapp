package com.horseapp.repository;

import java.util.Optional;
import java.util.Set;

import com.horseapp.model.User;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Set<User> findByCustomersId(Long clientId);
}
