package com.horseapp.service;

import com.horseapp.util.SessionManager;

import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final SessionManager sessionManager;

    public AuthorizationService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void validateUserAccess(long expectedUserId) {
        String role = (String) sessionManager.get("role");
        Long actualId = parseLong(sessionManager.get("id"));

        if (!"user".equals(role) || actualId == null || actualId != expectedUserId) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    public void validateCustomerAccess(long expectedCustomerId) {
        String role = (String) sessionManager.get("role");
        Long actualId = parseLong(sessionManager.get("id"));

        if (!"customer".equals(role) || actualId == null || actualId != expectedCustomerId) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    public String getLoggedInUsername() {
        Object username = sessionManager.get("username");
        if (username == null) throw new IllegalStateException("Not logged in");
        return username.toString();
    }

    public String getLoggedInRole() {
        Object role = sessionManager.get("role");
        if (role == null) throw new IllegalStateException("No user signed in");
        return role.toString();
    }

    public long getLoggedInId() {
        Object id = sessionManager.get("id");
        if (id == null) throw new IllegalStateException("No user signed in");
        return Long.parseLong(id.toString());
    }

    private Long parseLong(Object obj) {
        try {
            return (obj != null) ? Long.parseLong(obj.toString()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
