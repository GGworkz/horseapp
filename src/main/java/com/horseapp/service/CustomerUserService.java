package com.horseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import com.horseapp.repository.CustomerRepository;
import com.horseapp.repository.UserRepository;
import com.horseapp.model.User;
import com.horseapp.model.Customer;

@Service
public class CustomerUserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    // Get Customers for a User
    public Set<Customer> getCustomers(Long userId) {
        return customerRepository.findByUsersId(userId);
    }

    // Get Users for a Customer
    public Set<User> getUsers(Long customerId) {
        return userRepository.findByCustomersId(customerId);
    }

    // Add a user to a customer
    public ResponseEntity<String> addUserToCustomer(Long customerId, Long userId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (customerOpt.isPresent() && userOpt.isPresent()) {
            Customer customer = customerOpt.get();
            User user = userOpt.get();
            if (user.getCustomers().contains(customer)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Veterinarian already enrolled");
            }
            customer.getUsers().add(user);
            customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.OK).body("User added to customer successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer or User not found");
    }
}

