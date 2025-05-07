package com.horseapp.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class ConsultationDetailId implements Serializable {
    private Long consultation;
    private Long product;

    public ConsultationDetailId() {}

    public ConsultationDetailId(Long consultation, Long product) {
        this.consultation = consultation;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsultationDetailId)) return false;
        ConsultationDetailId that = (ConsultationDetailId) o;
        return Objects.equals(consultation, that.consultation) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consultation, product);
    }
}
