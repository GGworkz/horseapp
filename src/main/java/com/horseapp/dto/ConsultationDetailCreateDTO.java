package com.horseapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultationDetailCreateDTO {
    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
