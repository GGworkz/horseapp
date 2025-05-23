package com.horseapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.horseapp.dto.CustomerResponseDTO;
import com.horseapp.dto.CustomerUserResponseDTO;
import com.horseapp.dto.UserCustomerResponseDTO;
import com.horseapp.dto.UserResponseDTO;
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

    public CustomerUserResponseDTO getCustomerUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Customer> customers = customerRepository.findByUsersId(userId);

        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());

        List<CustomerResponseDTO> customerDTOs = customers.stream()
                .map(c -> {
                    CustomerResponseDTO dto = new CustomerResponseDTO();
                    dto.setId(c.getId());
                    dto.setUsername(c.getUsername());
                    dto.setEmail(c.getEmail());
                    dto.setFirstName(c.getFirstName());
                    dto.setLastName(c.getLastName());
                    dto.setPhone(c.getPhone());
                    return dto;
                })
                .collect(Collectors.toList());

        return new CustomerUserResponseDTO(userDTO, customerDTOs);
    }

    public UserCustomerResponseDTO getUsersByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Set<User> users = userRepository.findByCustomersId(customerId);

        CustomerResponseDTO customerDTO = new CustomerResponseDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setUsername(customer.getUsername());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setPhone(customer.getPhone());

        List<UserResponseDTO> userDTOs = users.stream()
                .map(u -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(u.getId());
                    dto.setUsername(u.getUsername());
                    dto.setFirstName(u.getFirstName());
                    dto.setLastName(u.getLastName());
                    dto.setEmail(u.getEmail());
                    dto.setPhone(u.getPhone());
                    return dto;
                })
                .collect(Collectors.toList());

        return new UserCustomerResponseDTO(customerDTO, userDTOs);
    }

    public Set<Customer> getCustomerEntitiesByUserId(Long userId) {
        return customerRepository.findByUsersId(userId);
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

