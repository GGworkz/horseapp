package com.horseapp.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
@Table(name = "consultation_details")
@IdClass(ConsultationDetailId.class)
public class ConsultationDetail implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    @JsonIgnore
    private Consultation consultation;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProductCatalog product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horse_id", nullable = false)
    @JsonIgnore
    private Horse horse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private int quantity = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsultationDetail)) return false;
        ConsultationDetail that = (ConsultationDetail) o;
        return Objects.equals(consultation != null ? consultation.getId() : null,
                that.consultation != null ? that.consultation.getId() : null) &&
                Objects.equals(product != null ? product.getId() : null,
                        that.product != null ? that.product.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                consultation != null ? consultation.getId() : null,
                product != null ? product.getId() : null
        );
    }
}
