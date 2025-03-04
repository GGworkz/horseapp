package com.horseapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.horseapp.model.User;
import com.horseapp.service.UserService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> postUserSignUp(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postUserSignIn(@RequestBody User user) {
        return userService.logIn(user);
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
