package com.horseapp.controller;

import com.horseapp.model.User;
import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User management APIs")
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
