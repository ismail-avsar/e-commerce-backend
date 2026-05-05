package com.bandage.ecommerce.dto;

public record AuthResponse(String token, String name, String email, Long roleId) {
}
