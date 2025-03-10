package com.horseapp.model;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name="clients")
public class Client {
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
        Client other = (Client) obj;
        return this.username.equals(other.username) && this.email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @ManyToMany
    @JoinTable(
      name = "client_user", 
      joinColumns = @JoinColumn(name = "client_id"), 
      inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference 
    @JsonIgnore
    private Set<User> users;
}
