package com.horseapp.model;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Set<Client> clients;
}
