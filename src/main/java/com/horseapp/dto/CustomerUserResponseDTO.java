package com.horseapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomerUserResponseDTO {
    private UserResponseDTO user;
    private List<CustomerResponseDTO> customers;

    public CustomerUserResponseDTO(UserResponseDTO user, List<CustomerResponseDTO> customers) {
        this.user = user;
        this.customers = customers;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public List<CustomerResponseDTO> getCustomers() {
        return customers;
    }
}

