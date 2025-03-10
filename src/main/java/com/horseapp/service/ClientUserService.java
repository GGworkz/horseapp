package com.horseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import com.horseapp.repository.ClientRepository;
import com.horseapp.repository.UserRepository;
import com.horseapp.model.User;
import com.horseapp.model.Client;

@Service
public class ClientUserService {

    @Autowired
    private ClientRepository clientRepository;

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
}

