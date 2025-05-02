package com.horseapp.service;

import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final SessionService sessionService;

    public AuthorizationService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void validateUserAccess(long expectedUserId) {
        String role = (String) sessionService.getAttribute("role");
        Long actualId = parseLong(sessionService.getAttribute("id"));

        if (!"user".equals(role) || actualId == null || actualId != expectedUserId) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    public void validateCustomerAccess(long expectedCustomerId) {
        String role = (String) sessionService.getAttribute("role");
        Long actualId = parseLong(sessionService.getAttribute("id"));

        if (!"customer".equals(role) || actualId == null || actualId != expectedCustomerId) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    private Long parseLong(Object obj) {
        try {
            return (obj != null) ? Long.parseLong(obj.toString()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getLoggedInUsername() {
        Object username = sessionService.getAttribute("username");
        if (username == null) throw new IllegalStateException("Not logged in");
        return username.toString();
    }

    public String getLoggedInRole() {
        Object role = sessionService.getAttribute("role");
        if (role == null) throw new IllegalStateException("No user signed in");
        return role.toString();
    }

    public long getLoggedInId() {
        Object id = sessionService.getAttribute("id");
        if (id == null) throw new IllegalStateException("No user signed in");
        return Long.parseLong(id.toString());
    }
}
