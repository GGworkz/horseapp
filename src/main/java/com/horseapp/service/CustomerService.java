package com.horseapp.service;

import java.util.NoSuchElementException;

import jakarta.persistence.EntityNotFoundException;

import com.horseapp.model.Customer;
import com.horseapp.repository.CustomerRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String create(Customer customer) {
        for (Customer currentCustomer : customerRepository.findAll()) {
            if (customer.equals(currentCustomer)) {
                return "exists";
            }
        }

        if (customer.getPassword().length() > 72) {
            return "too_long";
        }

        String hashedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(hashedPassword);
        customerRepository.save(customer);
        return "created";
    }

    public boolean isLoginValid(Customer customer) {
        Customer storedCustomer = customerRepository.findByUsername(customer.getUsername()).orElse(null);
        return storedCustomer != null && passwordEncoder.matches(customer.getPassword(), storedCustomer.getPassword());
    }

    public Customer update(Customer customer) {
        if (!customerRepository.existsById(customer.getId())) {
            throw new EntityNotFoundException("Customer not found");
        }
        return customerRepository.save(customer);
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

    public Customer getByIdOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }
}
