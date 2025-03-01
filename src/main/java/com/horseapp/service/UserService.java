package com.horseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
        List<User> users = userRepository.findAll();
        for (User otherUser : users) {
            if (user.hasSameUserName(otherUser)) {
                return new ResponseEntity<>("User Already Exists. Cannot Create", HttpStatus.BAD_REQUEST);
            }
        }

        String password = user.getPassword();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            String hash = hexString.toString();
            user.setPassword(hash);
            userRepository.save(user);
            return new ResponseEntity<>("User has been created", HttpStatus.OK);
        } catch (NoSuchAlgorithmException exception) {
            return new ResponseEntity<>("There was an issue with your request", HttpStatus.BAD_REQUEST);
        }
    }

    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    public ResponseEntity<String> logIn(User user) {
        List<User> allUsers = userRepository.findAll();
        boolean doesUserNameExist = false;
        for (User otherUser : allUsers) {
            if (user.hasSameUserName(otherUser)) {
                doesUserNameExist = true;
            }
        }

        if (!doesUserNameExist) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }

        String password = user.getPassword();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            String hash = hexString.toString();
            user.setPassword(hash);
            if (userRepository.findAll().contains(user)) {
                return new ResponseEntity<>("Successful login", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }
}
