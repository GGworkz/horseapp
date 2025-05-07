package com.horseapp.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Size(max = 72, message = "Password must be 72 characters or fewer")
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
