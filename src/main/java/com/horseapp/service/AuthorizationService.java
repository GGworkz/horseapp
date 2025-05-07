package com.horseapp.service;

import com.horseapp.security.AccessGuard;
import com.horseapp.util.SessionManager;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final SessionManager sessionManager;
    private final AccessGuard accessGuard;

    public AuthorizationService(SessionManager sessionManager, @Lazy AccessGuard accessGuard) {
        this.sessionManager = sessionManager;
        this.accessGuard = accessGuard;
    }

    public void validateUserAccess(long expectedUserId) {
        if (!accessGuard.hasUserAccess(expectedUserId)) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    public void validateCustomerAccess(long expectedCustomerId) {
        if (!accessGuard.hasCustomerAccess(expectedCustomerId)) {
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
