package com.horseapp.service;

import java.util.NoSuchElementException;

import com.horseapp.model.User;
import com.horseapp.model.Customer;
import com.horseapp.util.SessionManager;

import org.springframework.stereotype.Service;

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

    public boolean signInUser(User user) {
        if (sessionManager.isLoggedIn()) {
            return false;
        }

        try {
            User found = userService.findByUsername(user.getUsername());
            if (userService.isLoginValid(user)) {
                sessionManager.create(found.getId(), found.getUsername(), "user", 360);
                return true;
            }
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean signInCustomer(Customer customer) {
        if (sessionManager.isLoggedIn()) {
            return false;
        }

        try {
            Customer found = customerService.findByUsername(customer.getUsername());
            if (customerService.isLoginValid(customer)) {
                sessionManager.create(found.getId(), found.getUsername(), "customer", 360);
                return true;
            }
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void signOut() {
        sessionManager.destroy();
    }
}
