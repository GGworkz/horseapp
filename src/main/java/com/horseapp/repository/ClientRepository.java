package com.horseapp.repository;

import com.horseapp.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);
    Set<Client> findByUsersId(Long userId);
}
