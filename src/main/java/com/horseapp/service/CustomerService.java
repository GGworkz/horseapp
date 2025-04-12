package com.horseapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.horseapp.model.Customer;
import com.horseapp.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<String> create(Customer customer) {
        // Does customer exist
        for (Customer currentCustomer : customerRepository.findAll()) {
            if (customer.equals(currentCustomer)) {
                return new ResponseEntity<>("Username or Email Already Exists.", HttpStatus.BAD_REQUEST);
            }
        }

        // Hash the customer's password
        String password = customer.getPassword();
        String hashedPassword = "";
        try {
            hashedPassword = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        customer.setPassword(hashedPassword);

        // Save and return a successful response
        customerRepository.save(customer);
        return new ResponseEntity<>("Customer has been created", HttpStatus.OK);
    }

    public Customer findById(long id) {
        return customerRepository.findById(id).get();
    }

    public ResponseEntity<String> logIn(@RequestBody Customer customer) {
        // Check if customer exists
        Customer storedCustomer = null;
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            storedCustomer = customerRepository.findByUsername(customer.getUsername()).get();
        } else {
            return new ResponseEntity<>("Customer does not exist", HttpStatus.BAD_REQUEST);
        }

        // Generate password hash
        String password = customer.getPassword();
        String hashedPassword = "";
        try {
            hashedPassword = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

        // Validate hashed password
        if (!hashedPassword.equals(storedCustomer.getPassword())) {
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successful login", HttpStatus.OK);
    }

    public String getPasswordHash(String password) throws NoSuchAlgorithmException {
        // Generate password hash
        StringBuilder hexString = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));
    }
}
