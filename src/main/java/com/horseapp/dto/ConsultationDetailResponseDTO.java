package com.horseapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsultationDetailResponseDTO {
    private Long consultationId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productType;
    private int quantity;
}
