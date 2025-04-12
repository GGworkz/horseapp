package com.horseapp.controller;

import com.horseapp.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.model.Customer;
import com.horseapp.service.CustomerService;

@Tag(name = "Customer", description = "Customer management APIs")
@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final SessionService sessionService;


    public CustomerController(CustomerService customerService, SessionService sessionService) {
        this.customerService = customerService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postCustomerSignUp(@RequestBody Customer user) {
        return customerService.create(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postCustomerSignIn(@RequestBody Customer user) {
        return sessionService.handleSignIn(customerService, user);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postCustomerSignOut(@RequestBody Customer user) {
        return sessionService.handleSignOut();
    }

    @GetMapping("")
    public ResponseEntity<Customer> getCurrentCustomer() {
        try {
            return ResponseEntity.ok(customerService.findByUsername(sessionService.getLoggedInUsername()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
