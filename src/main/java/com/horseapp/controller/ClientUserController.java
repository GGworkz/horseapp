package com.horseapp.controller;

import com.horseapp.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.service.ClientUserService;
import com.horseapp.model.Client;
import com.horseapp.model.User;

import java.util.Set;

@RestController
@RequestMapping
public class ClientUserController {

    @Autowired
    private ClientUserService clientUserService;
    private final SessionService sessionService;

    public ClientUserController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Endpoint to get clients for a user
    @GetMapping("/user/{userId}/clients")
    public ResponseEntity<?> getClients(@PathVariable Long userId) {
        try {
            String role = sessionService.getLoggedInRole();
            long id = sessionService.getLoggedInID();

            // Only allow access if logged-in user has role "user" and matches userId
            if (!role.equals("user") || id != userId) {
                throw new IllegalStateException();
            }

            Set<Client> clients = clientUserService.getClients(userId);
            return ResponseEntity.ok(clients);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }


    // Endpoint to get users for a client
    @GetMapping("/client/{clientId}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long clientId) {
        try {
            String role = sessionService.getLoggedInRole();
            long id = sessionService.getLoggedInID();

            // Only allow access if logged-in user is a client and matches clientId
            if (!role.equals("client") || id != clientId) {
                throw new IllegalStateException();
            }

            Set<User> users = clientUserService.getUsers(clientId);
            return ResponseEntity.ok(users);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }


    // Add a user to a client
    @PostMapping("/client/{clientId}/user/{userId}")
    public ResponseEntity<String> addUserToClient(@PathVariable Long clientId, @PathVariable Long userId) {
        try {
            String role = sessionService.getLoggedInRole();
            long id = sessionService.getLoggedInID();
            if (!role.equals("client") || id != clientId) {
                throw new IllegalStateException();
            }
            return clientUserService.addUserToClient(clientId, userId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }
    }
}
