package com.bandage.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRequest(
        @NotNull Long addressId,
        LocalDateTime orderDate,
        String cardNo,
        String cardName,
        Integer cardExpireMonth,
        Integer cardExpireYear,
        Integer cardCcv,
        @NotNull BigDecimal price,
        @NotNull List<OrderProductRequest> products
) {
}
