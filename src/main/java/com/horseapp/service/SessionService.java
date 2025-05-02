package com.horseapp.service;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {

    public HttpSession getSession(boolean create) {
        ServletRequestAttributes attr =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attr != null) ? attr.getRequest().getSession(create) : null;
    }

    public boolean isUserLoggedIn() {
        HttpSession session = getSession(false);
        return session != null && session.getAttribute("username") != null;
    }

    public void createSession(long id, String username, String role, int timeout) {
        HttpSession session = getSession(true);
        if (session != null) {
            session.setAttribute("id", id);
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            session.setMaxInactiveInterval(timeout);
        }
    }

    public void destroySession() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public Object getAttribute(String key) {
        HttpSession session = getSession(false);
        return (session != null) ? session.getAttribute(key) : null;
    }

    public void setAttribute(String key, Object value) {
        HttpSession session = getSession(true);
        if (session != null) {
            session.setAttribute(key, value);
        }
    }

}

