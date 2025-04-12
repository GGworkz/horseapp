package com.horseapp.repository;

import com.horseapp.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
    Set<Customer> findByUsersId(Long userId);

    //@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c JOIN c.users u WHERE c.id = :customerId AND u.id = :userId")
    //boolean existsByCustomerIdAndUserId(@Param("customerId") Long customerId, @Param("userId") Long userId);
}
