package com.horseapp.controller;

import com.horseapp.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.model.Client;
import com.horseapp.service.ClientService;

@Tag(name = "Client", description = "Client management APIs")
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final SessionService sessionService;


    public ClientController(ClientService clientService, SessionService sessionService) {
        this.clientService = clientService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postClientSignUp(@RequestBody Client user) {
        return clientService.create(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postClientSignIn(@RequestBody Client user) {
        return sessionService.handleSignIn(clientService, user);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postClientSignOut(@RequestBody Client user) {
        return sessionService.handleSignOut();
    }

    @GetMapping("")
    public ResponseEntity<Client> getCurrentClient() {
        try {
            return ResponseEntity.ok(clientService.findByUsername(sessionService.getLoggedInUsername()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
