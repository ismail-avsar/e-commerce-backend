package com.bandage.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime orderDate,
        BigDecimal price,
        String cardNo,
        List<OrderItemResponse> products
) {
}
