package com.horseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import com.horseapp.repository.CustomerRepository;
import com.horseapp.repository.UserRepository;
import com.horseapp.model.User;
import com.horseapp.model.Client;

@Service
public class ClientUserService {

    @Autowired
    private CustomerRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    // Get Clients for a User
    public Set<Client> getClients(Long userId) {
        return clientRepository.findByUsersId(userId);
    }

    // Get Users for a Client
    public Set<User> getUsers(Long clientId) {
        return userRepository.findByClientsId(clientId);
    }

    // Add a user to a client
    public ResponseEntity<String> addUserToClient(Long clientId, Long userId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (clientOpt.isPresent() && userOpt.isPresent()) {
            Client client = clientOpt.get();
            User user = userOpt.get();
            if (user.getClients().contains(client)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Veterinarian already enrolled");
            }
            client.getUsers().add(user);
            clientRepository.save(client);
            return ResponseEntity.status(HttpStatus.OK).body("User added to client successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client or User not found");
    }
}

