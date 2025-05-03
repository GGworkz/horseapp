package com.horseapp.controller;

import com.horseapp.model.Customer;
import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer", description = "Customer management APIs")
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
