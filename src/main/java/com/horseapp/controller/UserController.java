package com.horseapp.controller;

import com.horseapp.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.model.User;
import com.horseapp.service.UserService;

@Tag(name = "User", description = "User management APIs")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;


    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postUserSignUp(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postUserSignIn(@RequestBody User user) {
        return sessionService.handleSignIn(userService, user);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postUserSignOut(@RequestBody User user) {
        return sessionService.handleSignOut();
    }

    @GetMapping("")
    public ResponseEntity<User> getCurrentUser() {
        try {
            return ResponseEntity.ok(userService.findByUsername(sessionService.getLoggedInUsername()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
