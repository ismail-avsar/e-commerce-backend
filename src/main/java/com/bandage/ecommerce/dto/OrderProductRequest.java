package com.bandage.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

public record OrderProductRequest(@NotNull Long productId, @NotNull Integer count, String detail) {
}
