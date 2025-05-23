package com.horseapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserCustomerResponseDTO {
    private CustomerResponseDTO customer;
    private List<UserResponseDTO> users;

    public UserCustomerResponseDTO(CustomerResponseDTO customer, List<UserResponseDTO> users) {
        this.customer = customer;
        this.users = users;
    }

    public CustomerResponseDTO getCustomer() {
        return customer;
    }

    public List<UserResponseDTO> getUsers() {
        return users;
    }
}

