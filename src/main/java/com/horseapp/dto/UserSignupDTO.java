package com.horseapp.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UserSignupDTO {
    private String username;

    @Size(max = 72, message = "Password must be 72 characters or fewer")
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
