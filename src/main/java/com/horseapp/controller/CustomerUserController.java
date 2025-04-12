package com.horseapp.controller;

import com.horseapp.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.service.CustomerUserService;
import com.horseapp.model.Customer;
import com.horseapp.model.User;

import java.util.Set;

@Tag(name = "User-Customer", description = "User Customer Relationship APIs")
@RestController
@RequestMapping
public class CustomerUserController {

    @Autowired
    private CustomerUserService customerUserService;
    private final SessionService sessionService;

    public CustomerUserController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Endpoint to get customers for a user
    @GetMapping("/user/{userId}/customers")
    public ResponseEntity<?> getCustomers(@PathVariable Long userId) {
        try {
            String role = sessionService.getLoggedInRole();
            long id = sessionService.getLoggedInID();

            // Only allow access if logged-in user has role "user" and matches userId
            if (!role.equals("user") || id != userId) {
                throw new IllegalStateException();
            }

            Set<Customer> customers = customerUserService.getCustomers(userId);
            return ResponseEntity.ok(customers);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }


    // Endpoint to get users for a customer
    @GetMapping("/customer/{customerId}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long customerId) {
        try {
            String role = sessionService.getLoggedInRole();
            long id = sessionService.getLoggedInID();

            // Only allow access if logged-in user is a customer and matches customerId
            if (!role.equals("customer") || id != customerId) {
                throw new IllegalStateException();
            }

            Set<User> users = customerUserService.getUsers(customerId);
            return ResponseEntity.ok(users);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }


    // Add a user to a customer
    @PostMapping("/customer/{customerId}/user/{userId}")
    public ResponseEntity<String> addUserToCustomer(@PathVariable Long customerId, @PathVariable Long userId) {
        try {
            String role = sessionService.getLoggedInRole();
            long id = sessionService.getLoggedInID();
            if (!role.equals("customer") || id != customerId) {
                throw new IllegalStateException();
            }
            return customerUserService.addUserToCustomer(customerId, userId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }
}
