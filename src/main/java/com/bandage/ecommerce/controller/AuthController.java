package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.AuthResponse;
import com.bandage.ecommerce.dto.LoginRequest;
import com.bandage.ecommerce.dto.RoleResponse;
import com.bandage.ecommerce.dto.SignupRequest;
import com.bandage.ecommerce.service.AuthService;
import com.bandage.ecommerce.service.CurrentUserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final CurrentUserService currentUserService;

    public AuthController(AuthService authService, CurrentUserService currentUserService) {
        this.authService = authService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/roles")
    public List<RoleResponse> roles() {
        return authService.getRoles();
    }

    @PostMapping("/signup")
    public AuthResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/verify")
    public AuthResponse verify() {
        return authService.verify(currentUserService.getCurrentUser());
    }
}
