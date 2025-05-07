package com.horseapp.controller;

import java.util.Set;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.model.Customer;
import com.horseapp.model.User;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User-Customer", description = "User Customer relationship management")
@RestController
@RequestMapping
public class CustomerUserController {

    private final CustomerUserService customerUserService;
    private final AuthorizationService authorizationService;

    public CustomerUserController(CustomerUserService customerUserService,
                                  AuthorizationService authorizationService) {
        this.customerUserService = customerUserService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/user/{userId}/customers")
    public ResponseEntity<?> getCustomers(@PathVariable Long userId) {
        try {
            authorizationService.validateUserAccess(userId);
            Set<Customer> customers = customerUserService.getCustomers(userId);
            return ResponseEntity.ok(customers);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }

    @GetMapping("/customer/{customerId}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long customerId) {
        try {
            authorizationService.validateCustomerAccess(customerId);
            Set<User> users = customerUserService.getUsers(customerId);
            return ResponseEntity.ok(users);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }

    @PostMapping("/customer/{customerId}/user/{userId}")
    public ResponseEntity<String> addUserToCustomer(@PathVariable Long customerId, @PathVariable Long userId) {
        try {
            authorizationService.validateCustomerAccess(customerId);

            String result = customerUserService.addUserToCustomer(customerId, userId);

            return switch (result) {
                case "already_enrolled" -> ResponseEntity.status(HttpStatus.CONFLICT).body("Veterinarian already enrolled");
                case "success" -> ResponseEntity.ok("User added to customer successfully");
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer or User not found");
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
            };

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }
}
