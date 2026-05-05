package com.bandage.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        Long id,
        @NotBlank String title,
        @NotBlank String name,
        @NotBlank String surname,
        @NotBlank String phone,
        @NotBlank String city,
        @NotBlank String district,
        @NotBlank String neighborhood,
        String address
) {
}
