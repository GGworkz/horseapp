package com.horseapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.horseapp.model.User;
import com.horseapp.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<String> postUserSignUp(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id) {
        return userService.findById(id);
    }
    
    @PostMapping("/user/signin")
    public ResponseEntity<String> postUserSignIn(@RequestBody User user) {
        return userService.logIn(user);
    }
    
}
