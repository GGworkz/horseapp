package com.horseapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.Objects;

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
