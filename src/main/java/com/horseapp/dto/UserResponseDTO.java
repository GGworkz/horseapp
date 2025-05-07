package com.horseapp.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
