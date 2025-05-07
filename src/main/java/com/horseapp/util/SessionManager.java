package com.horseapp.util;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionManager {

    public HttpSession getSession(boolean create) {
        var attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attr != null) ? attr.getRequest().getSession(create) : null;
    }

    public void create(long id, String username, String role, int timeout) {
        HttpSession session = getSession(true);
        if (session != null) {
            session.setAttribute("id", id);
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            session.setMaxInactiveInterval(timeout);
        }
    }

    public void destroy() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public Object get(String key) {
        HttpSession session = getSession(false);
        return (session != null) ? session.getAttribute(key) : null;
    }

    public boolean isLoggedIn() {
        HttpSession session = getSession(false);
        return session != null && session.getAttribute("username") != null;
    }
}
