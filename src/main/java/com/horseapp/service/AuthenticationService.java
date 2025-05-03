package com.horseapp.service;

import com.horseapp.model.User;
import com.horseapp.model.Customer;
import com.horseapp.util.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthenticationService {

    private final SessionManager sessionManager;
    private final UserService userService;
    private final CustomerService customerService;

    public AuthenticationService(SessionManager sessionManager,
                                 UserService userService,
                                 CustomerService customerService) {
        this.sessionManager = sessionManager;
        this.userService = userService;
        this.customerService = customerService;
    }

    public ResponseEntity<String> signInUser(User user) {
        if (sessionManager.isLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already logged in");
        }

        try {
            User found = userService.findByUsername(user.getUsername());
            ResponseEntity<String> result = userService.logIn(user);
            if (result.getStatusCode() == HttpStatus.OK) {
                sessionManager.create(found.getId(), found.getUsername(), "user", 360);
            }
            return result;
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    public ResponseEntity<String> signInCustomer(Customer customer) {
        if (sessionManager.isLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already logged in");
        }

        try {
            Customer found = customerService.findByUsername(customer.getUsername());
            ResponseEntity<String> result = customerService.logIn(customer);
            if (result.getStatusCode() == HttpStatus.OK) {
                sessionManager.create(found.getId(), found.getUsername(), "customer", 360);
            }
            return result;
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }


    public ResponseEntity<String> signOut() {
        sessionManager.destroy();
        return ResponseEntity.ok("Signed out successfully");
    }
}
