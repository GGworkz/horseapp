package com.horseapp.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CustomerSignupDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Size(max = 72, message = "Password must be 72 characters or fewer")
    private String password;

    private String firstName;
    private String lastName;

    @Email
    private String email;

    private String phone;
}
