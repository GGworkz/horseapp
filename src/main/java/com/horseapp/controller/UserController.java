package com.horseapp.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.model.User;
import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User management")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;

    public UserController(UserService userService,
                          AuthenticationService authenticationService,
                          AuthorizationService authorizationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postUserSignUp(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postUserSignIn(@Valid @RequestBody User user) {
        return authenticationService.signInUser(user);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postUserSignOut() {
        return authenticationService.signOut();
    }

    @GetMapping("")
    public ResponseEntity<User> getCurrentUser() {
        try {
            String username = authorizationService.getLoggedInUsername(); // new method
            return ResponseEntity.ok(userService.findByUsername(username));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody User updates) {
        try {
            long userId = authorizationService.getLoggedInId();
            String role = authorizationService.getLoggedInRole();

            if (!"user".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only users can update their account");
            }

            User existing = userService.findById(userId);
            existing.setPassword(updates.getPassword());
            existing.setFirstName(updates.getFirstName());
            existing.setLastName(updates.getLastName());
            existing.setEmail(updates.getEmail());
            existing.setPhone(updates.getPhone());

            return ResponseEntity.ok(userService.update(existing));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCurrentUser() {
        try {
            long userId = authorizationService.getLoggedInId();
            String role = authorizationService.getLoggedInRole();

            if (!"user".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only users can delete their account");
            }

            userService.deleteById(userId);
            authenticationService.signOut();
            return ResponseEntity.ok("User deleted, session ended");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }
}
