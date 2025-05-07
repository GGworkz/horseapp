package com.horseapp.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.dto.UserLoginDTO;
import com.horseapp.dto.UserSignupDTO;
import com.horseapp.dto.UserUpdateDTO;
import com.horseapp.dto.UserResponseDTO;
import com.horseapp.model.User;
import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.UserService;
import com.horseapp.util.SessionManager;

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
    private final SessionManager sessionManager;

    public UserController(UserService userService,
                          AuthenticationService authenticationService,
                          AuthorizationService authorizationService, SessionManager sessionManager) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postUserSignUp(@Valid @RequestBody UserSignupDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        String result = userService.create(user);

        return switch (result) {
            case "exists" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or Email Already Exists.");
            case "too_long" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password too long");
            case "created" -> ResponseEntity.ok("User has been created");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        };
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postUserSignIn(@Valid @RequestBody UserLoginDTO dto) {
        if (sessionManager.isLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already logged in");
        }

        User loginUser = new User();
        loginUser.setUsername(dto.getUsername());
        loginUser.setPassword(dto.getPassword());

        boolean success = authenticationService.signInUser(loginUser);
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }

        return ResponseEntity.ok("Successful login");
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postCustomerSignOut() {
        authenticationService.signOut();
        return ResponseEntity.ok("Signed out successfully");
    }

    @GetMapping("")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        try {
            String username = authorizationService.getLoggedInUsername();
            User user = userService.findByUsername(username);

            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());

            return ResponseEntity.ok(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UserUpdateDTO updates) {
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
