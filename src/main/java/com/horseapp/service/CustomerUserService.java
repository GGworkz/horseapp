package com.horseapp.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.horseapp.repository.CustomerRepository;
import com.horseapp.repository.UserRepository;
import com.horseapp.model.User;
import com.horseapp.model.Customer;

@Service
public class CustomerUserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Set<Customer> getCustomers(Long userId) {
        return customerRepository.findByUsersId(userId);
    }

    public Set<User> getUsers(Long customerId) {
        return userRepository.findByCustomersId(customerId);
    }

    public String addUserToCustomer(Long customerId, Long userId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (customerOpt.isPresent() && userOpt.isPresent()) {
            Customer customer = customerOpt.get();
            User user = userOpt.get();

            if (user.getCustomers().contains(customer)) {
                return "already_enrolled";
            }

            customer.getUsers().add(user);
            customerRepository.save(customer);
            return "success";
        }

        return "not_found";
    }

    public boolean removeUserFromCustomer(Long customerId, Long userId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (customerOpt.isPresent() && userOpt.isPresent()) {
            Customer customer = customerOpt.get();
            User user = userOpt.get();

            if (customer.getUsers().remove(user)) {
                customerRepository.save(customer);
                return true;
            }
        }

        return false;
    }

}

