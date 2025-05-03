package com.horseapp.service;

import com.horseapp.model.Customer;
import com.horseapp.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> create(Customer customer) {
        for (Customer currentCustomer : customerRepository.findAll()) {
            if (customer.equals(currentCustomer)) {
                return new ResponseEntity<>("Username or Email Already Exists.", HttpStatus.BAD_REQUEST);
            }
        }

        if (customer.getPassword().length() > 72) {
            return new ResponseEntity<>("Password too long", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(hashedPassword);
        customerRepository.save(customer);
        return new ResponseEntity<>("Customer has been created", HttpStatus.OK);
    }

    public ResponseEntity<String> logIn(@RequestBody Customer customer) {
        Customer storedCustomer = customerRepository.findByUsername(customer.getUsername()).orElse(null);

        if (storedCustomer == null) {
            return new ResponseEntity<>("Customer does not exist", HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(customer.getPassword(), storedCustomer.getPassword())) {
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successful login", HttpStatus.OK);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer findById(long id) {
        return customerRepository.findById(id).get();
    }

    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));
    }
}
