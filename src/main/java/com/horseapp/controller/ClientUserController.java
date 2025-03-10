package com.horseapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    // Endpoint to get clients for a user
    @GetMapping("/user/{userId}/clients")
    public Set<Client> getClients(@PathVariable Long userId) {
        return clientUserService.getClients(userId);
    }

    // Endpoint to get users for a client
    @GetMapping("/client/{clientId}/users")
    public Set<User> getUsers(@PathVariable Long clientId) {
        return clientUserService.getUsers(clientId);
    }
}
