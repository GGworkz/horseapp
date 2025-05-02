package com.horseapp.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return this.username.equals(other.username) && this.email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    @ManyToMany(mappedBy = "users")
    @JsonBackReference 
    private Set<Customer> customers;
}
