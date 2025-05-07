package com.horseapp.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "horses")
public class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "horse_seq_gen")
    @SequenceGenerator(name = "horse_seq_gen", sequenceName = "horse_seq", allocationSize = 1)
    private Long id;

    private String name;
    private Long age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Horse)) return false;
        Horse horse = (Horse) o;
        return Objects.equals(name, horse.name)
                && Objects.equals(age, horse.age)
                && Objects.equals(customer != null ? customer.getId() : null,
                horse.customer != null ? horse.customer.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
