package com.horseapp.repository;

import com.horseapp.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);
    Set<Client> findByUsersId(Long userId);

    //@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c JOIN c.users u WHERE c.id = :clientId AND u.id = :userId")
    //boolean existsByClientIdAndUserId(@Param("clientId") Long clientId, @Param("userId") Long userId);
}
