package com.horseapp.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.dto.CustomerLoginDTO;
import com.horseapp.dto.CustomerSignupDTO;
import com.horseapp.dto.CustomerResponseDTO;
import com.horseapp.dto.CustomerUpdateDTO;
import com.horseapp.model.Customer;
import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerService;
import com.horseapp.util.SessionManager;

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
    private final SessionManager sessionManager;

    public CustomerController(CustomerService customerService,
                              AuthenticationService authenticationService,
                              AuthorizationService authorizationService, SessionManager sessionManager) {
        this.customerService = customerService;
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postCustomerSignUp(@Valid @RequestBody CustomerSignupDTO dto) {
        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());

        String result = customerService.create(customer);

        return switch (result) {
            case "exists" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or Email Already Exists.");
            case "too_long" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password too long");
            case "created" -> ResponseEntity.ok("Customer has been created");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        };
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postCustomerSignIn(@Valid @RequestBody CustomerLoginDTO dto) {
        if (sessionManager.isLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already logged in");
        }

        Customer loginCustomer = new Customer();
        loginCustomer.setUsername(dto.getUsername());
        loginCustomer.setPassword(dto.getPassword());

        boolean success = authenticationService.signInCustomer(loginCustomer);
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }

        return ResponseEntity.ok("Successful login");
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postCustomerSignOut() {
        authenticationService.signOut();
        return ResponseEntity.ok("Signed out successfully");
    }

    @GetMapping("")
    public ResponseEntity<CustomerResponseDTO> getCurrentCustomer() {
        try {
            String username = authorizationService.getLoggedInUsername();
            Customer customer = customerService.findByUsername(username);

            CustomerResponseDTO dto = new CustomerResponseDTO();
            dto.setId(customer.getId());
            dto.setUsername(customer.getUsername());
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setEmail(customer.getEmail());
            dto.setPhone(customer.getPhone());

            return ResponseEntity.ok(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCurrentCustomer(@Valid @RequestBody CustomerUpdateDTO updates) {
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
            return ResponseEntity.ok("Customer deleted, session ended");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }
}
