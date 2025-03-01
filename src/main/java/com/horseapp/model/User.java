package com.horseapp.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false; 
    
        User other = (User) obj;
    
        if (!this.username.equals(other.username)) return false;
        if (!this.password.equals(other.password)) return false;
    
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public boolean hasSameUserName(User other) {
        return this.username.equals(other.username);
    }
}
