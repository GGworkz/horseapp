package com.horseapp.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.model.Customer;
import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer", description = "Customer management")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;

    public CustomerController(CustomerService customerService,
                              AuthenticationService authenticationService,
                              AuthorizationService authorizationService) {
        this.customerService = customerService;
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postCustomerSignUp(@Valid @RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postCustomerSignIn(@Valid @RequestBody Customer customer) {
        return authenticationService.signInCustomer(customer);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postCustomerSignOut() {
        return authenticationService.signOut();
    }

    @GetMapping("")
    public ResponseEntity<Customer> getCurrentCustomer() {
        try {
            String username = authorizationService.getLoggedInUsername();
            return ResponseEntity.ok(customerService.findByUsername(username));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCurrentCustomer(@Valid @RequestBody Customer updates) {
        try {
            long customerId = authorizationService.getLoggedInId();
            String role = authorizationService.getLoggedInRole();

            if (!"customer".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can update their account");
            }

            Customer existing = customerService.findById(customerId);
            existing.setPassword(updates.getPassword());
            existing.setFirstName(updates.getFirstName());
            existing.setLastName(updates.getLastName());
            existing.setEmail(updates.getEmail());
            existing.setPhone(updates.getPhone());

            return ResponseEntity.ok(customerService.update(existing));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }


    @DeleteMapping
    public ResponseEntity<String> deleteCurrentCustomer() {
        try {
            long customerId = authorizationService.getLoggedInId();
            String role = authorizationService.getLoggedInRole();

            if (!"customer".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can delete their account");
            }

            customerService.deleteById(customerId);
            authenticationService.signOut();
            return ResponseEntity.ok("User deleted, session ended");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }

}
