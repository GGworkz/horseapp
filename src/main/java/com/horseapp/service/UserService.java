package com.horseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;
import com.horseapp.model.User;
import com.horseapp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> create(User user) {
        // Does user exist
        for (User currentUser : userRepository.findAll()) {
            if (user.equals(currentUser)) {
                return new ResponseEntity<>("Username or Email Already Exists.", HttpStatus.BAD_REQUEST);
            }
        }

        // Hash the user's password
        String password = user.getPassword();
        String hashedPassword = "";
        try {
            hashedPassword = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(hashedPassword);

        // Save and return a successful response
        userRepository.save(user);
        return new ResponseEntity<>("User has been created", HttpStatus.OK);
    }

    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    public ResponseEntity<String> logIn(User user) {
        // Check if user exists
        User storedUser = null;
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            storedUser = userRepository.findByUsername(user.getUsername()).get();
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }

        // Generate password hash
        String password = user.getPassword();
        String hashedPassword = "";
        try {
            hashedPassword = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

        // Validate hashed password
        if (!hashedPassword.equals(storedUser.getPassword())) {
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
}
