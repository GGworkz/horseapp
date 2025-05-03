package com.horseapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.horseapp.model.User;
import com.horseapp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> create(User user) {
        for (User currentUser : userRepository.findAll()) {
            if (user.equals(currentUser)) {
                return new ResponseEntity<>("Username or Email Already Exists.", HttpStatus.BAD_REQUEST);
            }
        }

        if (user.getPassword().length() > 72) {
            return new ResponseEntity<>("Password too long", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new ResponseEntity<>("User has been created", HttpStatus.OK);
    }

    public ResponseEntity<String> logIn(@RequestBody User user) {
        User storedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (storedUser == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successful login", HttpStatus.OK);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}
