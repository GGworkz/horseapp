package com.horseapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.model.User;
import com.horseapp.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postUserSignUp(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postUserSignIn(@RequestBody User user) {
        if (session.getAttribute("username") != null) {
            return new ResponseEntity<>("You are already logged in!", HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<String> response =  userService.logIn(user);
        if (response.getStatusCode() == HttpStatus.OK) {
            session.setAttribute("role", "vet");
            session.setAttribute("username", user.getUsername());
            session.setMaxInactiveInterval(5);
        }
        return response;
    }

    @PostMapping("/signout")
    public ResponseEntity<String> postUserSignOut(@RequestBody User user) {
        if (session.getAttribute("username") == null) {
            return new ResponseEntity<>("You are not signed in", HttpStatus.BAD_REQUEST);
        } else {
            session.invalidate();
        }
        return new ResponseEntity<>("You have successfully signed out!", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable long id) {
        try {
            return new ResponseEntity<>( userService.findById(id).toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }
}
