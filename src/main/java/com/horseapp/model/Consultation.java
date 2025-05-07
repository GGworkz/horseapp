package com.horseapp.model;

import java.time.ZonedDateTime;
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
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultation_seq_gen")
    @SequenceGenerator(name = "consultation_seq_gen", sequenceName = "consultation_seq", allocationSize = 1)
    private Long id;

    private ZonedDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horse_id", nullable = false)
    @JsonIgnore
    private Horse horse;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consultation)) return false;
        Consultation c = (Consultation) o;
        return Objects.equals(timestamp, c.timestamp) &&
                Objects.equals(horse != null ? horse.getId() : null,
                        c.horse != null ? c.horse.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }
}
