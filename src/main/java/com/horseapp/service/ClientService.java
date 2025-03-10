package com.horseapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.horseapp.model.Client;
import com.horseapp.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<String> create(Client client) {
        // Does client exist
        for (Client currentClient : clientRepository.findAll()) {
            if (client.equals(currentClient)) {
                return new ResponseEntity<>("Username or Email Already Exists.", HttpStatus.BAD_REQUEST);
            }
        }

        // Hash the client's password
        String password = client.getPassword();
        String hashedPassword = "";
        try {
            hashedPassword = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        client.setPassword(hashedPassword);

        // Save and return a successful response
        clientRepository.save(client);
        return new ResponseEntity<>("Client has been created", HttpStatus.OK);
    }

    public Client findById(long id) {
        return clientRepository.findById(id).get();
    }

    public ResponseEntity<String> logIn(@RequestBody Client client) {
        // Check if client exists
        Client storedClient = null;
        if (clientRepository.findByUsername(client.getUsername()).isPresent()) {
            storedClient = clientRepository.findByUsername(client.getUsername()).get();
        } else {
            return new ResponseEntity<>("Client does not exist", HttpStatus.BAD_REQUEST);
        }

        // Generate password hash
        String password = client.getPassword();
        String hashedPassword = "";
        try {
            hashedPassword = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

        // Validate hashed password
        if (!hashedPassword.equals(storedClient.getPassword())) {
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successful login", HttpStatus.OK);
    }

    public String getPasswordHash(String password) throws NoSuchAlgorithmException {
        // Generate password hash
        StringBuilder hexString = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public Client findByUsername(String username) {
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
    }
}
