package com.bandage.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreditCardRequest(
        Long id,
        @NotBlank String cardNo,
        @NotNull Integer expireMonth,
        @NotNull Integer expireYear,
        @NotBlank String nameOnCard
) {
}
