package com.horseapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsultationTotalDTO {
    private Long consultationId;
    private BigDecimal totalPrice;
}
