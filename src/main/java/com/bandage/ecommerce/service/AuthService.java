package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.AuthResponse;
import com.bandage.ecommerce.dto.LoginRequest;
import com.bandage.ecommerce.dto.RoleResponse;
import com.bandage.ecommerce.dto.SignupRequest;
import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.entity.Role;
import com.bandage.ecommerce.entity.Store;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.AppUserRepository;
import com.bandage.ecommerce.repository.RoleRepository;
import com.bandage.ecommerce.repository.StoreRepository;
import com.bandage.ecommerce.security.JwtService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final RoleRepository roleRepository;
    private final AppUserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            RoleRepository roleRepository,
            AppUserRepository userRepository,
            StoreRepository storeRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<RoleResponse> getRoles() {
        return roleRepository.findAll().stream().map(DtoMapper::role).toList();
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException("Email is already registered.", HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new ApiException("Role not found.", HttpStatus.BAD_REQUEST));

        AppUser user = new AppUser();
        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        AppUser savedUser = userRepository.save(user);

        if ("store".equals(role.getCode())) {
            if (request.store() == null) {
                throw new ApiException("Store information is required for store role.", HttpStatus.BAD_REQUEST);
            }
            Store store = new Store();
            store.setName(request.store().name());
            store.setPhone(request.store().phone());
            store.setTaxNo(request.store().taxNo());
            store.setBankAccount(request.store().bankAccount());
            store.setUser(savedUser);
            storeRepository.save(store);
        }

        return authResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException("Invalid email or password.", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApiException("Invalid email or password.", HttpStatus.UNAUTHORIZED);
        }

        return authResponse(user);
    }

    public AuthResponse verify(AppUser user) {
        return authResponse(user);
    }

    private AuthResponse authResponse(AppUser user) {
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().getId());
    }
}
