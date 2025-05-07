package com.horseapp.dto;

import lombok.Data;

@Data
public class HorseUpdateDTO {
    private String name;
    private Long age;
    private Long newOwnerId;
}

