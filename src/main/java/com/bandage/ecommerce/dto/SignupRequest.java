package com.bandage.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 3) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull Long roleId,
        StoreSignupRequest store
) {
}
