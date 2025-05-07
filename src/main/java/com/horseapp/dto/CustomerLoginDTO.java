package com.horseapp.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CustomerLoginDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
