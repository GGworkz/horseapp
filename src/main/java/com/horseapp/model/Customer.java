package com.horseapp.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name="customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @SequenceGenerator(name = "customer_seq_gen", sequenceName = "customer_seq", allocationSize = 1)
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
        Customer other = (Customer) obj;
        return this.username.equals(other.username) && this.email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @ManyToMany
    @JoinTable(
      name = "customer_user",
      joinColumns = @JoinColumn(name = "customer_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference 
    @JsonIgnore
    private Set<User> users;
}
