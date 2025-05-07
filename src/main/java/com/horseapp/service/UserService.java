package com.horseapp.service;

import java.util.NoSuchElementException;
import jakarta.persistence.EntityNotFoundException;

import com.horseapp.model.User;
import com.horseapp.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String create(User user) {
        for (User currentUser : userRepository.findAll()) {
            if (user.equals(currentUser)) {
                return "exists";
            }
        }

        if (user.getPassword().length() > 72) {
            return "too_long";
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "created";
    }

    public boolean isLoginValid(User user) {
        User storedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        return storedUser != null && passwordEncoder.matches(user.getPassword(), storedUser.getPassword());
    }

    public User update(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException("User not found");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
