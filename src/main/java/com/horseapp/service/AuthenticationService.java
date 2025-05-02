package com.horseapp.service;

import com.horseapp.model.User;
import com.horseapp.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final SessionService sessionService;
    private final UserService userService;
    private final CustomerService customerService;

    public AuthenticationService(SessionService sessionService,
                                 UserService userService,
                                 CustomerService customerService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.customerService = customerService;
    }

    public ResponseEntity<String> signInUser(User user) {
        if (sessionService.isUserLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already logged in");
        }
        User found = userService.findByUsername(user.getUsername());
        ResponseEntity<String> result = userService.logIn(user);
        if (result.getStatusCode() == HttpStatus.OK) {
            sessionService.createSession(found.getId(), found.getUsername(), "user", 360);
        }
        return result;
    }

    public ResponseEntity<String> signInCustomer(Customer customer) {
        if (sessionService.isUserLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already logged in");
        }
        Customer found = customerService.findByUsername(customer.getUsername());
        ResponseEntity<String> result = customerService.logIn(customer);
        if (result.getStatusCode() == HttpStatus.OK) {
            sessionService.createSession(found.getId(), found.getUsername(), "customer", 360);
        }
        return result;
    }

    public ResponseEntity<String> signOut() {
        sessionService.destroySession();
        return ResponseEntity.ok("Signed out successfully");
    }
}
