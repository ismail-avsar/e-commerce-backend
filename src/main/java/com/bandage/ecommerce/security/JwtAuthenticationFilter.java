package com.bandage.ecommerce.security;

import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.repository.AppUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AppUserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, AppUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && !token.isBlank() && jwtService.isValid(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String email = jwtService.getEmail(token);
            userRepository.findByEmailIgnoreCase(email).ifPresent(this::authenticate);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(AppUser user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode().toUpperCase());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
