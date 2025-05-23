package com.horseapp.controller;

import java.util.Set;

import com.horseapp.dto.CustomerUserResponseDTO;
import com.horseapp.dto.UserCustomerResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.model.Customer;
import com.horseapp.model.User;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @GetMapping("/user/{userId}/customers")
    public ResponseEntity<?> getCustomers(@PathVariable Long userId) {
        try {
            authorizationService.validateUserAccess(userId);
            CustomerUserResponseDTO response = customerUserService.getCustomerUserByUserId(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @GetMapping("/customer/{customerId}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long customerId) {
        try {
            authorizationService.validateCustomerAccess(customerId);
            UserCustomerResponseDTO response = customerUserService.getUsersByCustomerId(customerId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
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

    @PreAuthorize("@accessGuard.hasCustomerAccess(#customerId)")
    @DeleteMapping("/customer/{customerId}/user/{userId}")
    public ResponseEntity<String> removeUserFromCustomer(@PathVariable Long customerId, @PathVariable Long userId) {
        try {
            authorizationService.validateCustomerAccess(customerId);

            boolean removed = customerUserService.removeUserFromCustomer(customerId, userId);
            if (removed) {
                return ResponseEntity.ok("User removed from customer successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not associated with customer");
            }

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }
}
