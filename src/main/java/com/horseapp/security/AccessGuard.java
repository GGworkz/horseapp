package com.horseapp.security;

import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerUserService;

import org.springframework.stereotype.Component;

@Component("accessGuard")
public class AccessGuard {

    private final AuthorizationService authorizationService;
    private final CustomerUserService customerUserService;

    public AccessGuard(AuthorizationService authorizationService, CustomerUserService customerUserService) {
        this.authorizationService = authorizationService;
        this.customerUserService = customerUserService;
    }

    public boolean hasUserAccess(Long userId) {
        return "user".equals(authorizationService.getLoggedInRole())
                && authorizationService.getLoggedInId() == userId;
    }

    public boolean hasCustomerAccess(Long customerId) {
        String role = authorizationService.getLoggedInRole();
        long id = authorizationService.getLoggedInId();

        return ("customer".equals(role) && id == customerId)
                || ("user".equals(role) && customerUserService.getCustomerEntitiesByUserId(id).stream()
                .anyMatch(c -> c.getId().equals(customerId)));
    }

}

