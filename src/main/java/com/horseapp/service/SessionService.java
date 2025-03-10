package com.horseapp.service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;
import com.horseapp.model.User;
import com.horseapp.model.Client;

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

    public void createSession(String username, String role, int timeout) {
        HttpSession session = getSession(true);
        if (session != null) {
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            session.setMaxInactiveInterval(timeout);
        }
    }

    // todo overload this bitch
    public ResponseEntity<String> handleSignIn(UserService userService, User user) {
        if (isUserLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You are already logged in!");
        }

        ResponseEntity<String> response = userService.logIn(user);
        if (response.getStatusCode() == HttpStatus.OK) {
            createSession(user.getUsername(), "vet", 5);
        }
        return response;
    }

    public ResponseEntity<String> handleSignIn(ClientService clientService, Client client) {
        if (isUserLoggedIn()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You are already logged in!");
        }

        ResponseEntity<String> response = clientService.logIn(client);
        if (response.getStatusCode() == HttpStatus.OK) {
            createSession(client.getUsername(), "client", 5);
        }
        return response;
    }

    public ResponseEntity<String> handleSignOut() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
            return ResponseEntity.ok("Signed out successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active session found.");
    }

    public String getLoggedInUsername() {
        HttpSession session = getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            throw new IllegalStateException("No user signed in");
        }
        return session.getAttribute("username").toString();
    }
}

