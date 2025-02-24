package com.horseapp.rest_demo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@Entity
@Table(name = "personbelongings")
class PersonBelongings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "belongings_id_seq")
    @SequenceGenerator(name = "belongings_id_seq", sequenceName = "belongings_id_seq", allocationSize = 1)
    private Long id;
    private Long personId;
    private String itemName;
}

@Repository
interface PersonBelongingsRepository extends JpaRepository<PersonBelongings, Long> {
}
