package com.horseapp.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ConsultationDetailUpdateDTO {
    @Min(1)
    private int quantity;
}
