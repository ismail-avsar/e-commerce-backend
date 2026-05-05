package com.bandage.ecommerce.service;

import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUser user)) {
            throw new ApiException("Login required.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
