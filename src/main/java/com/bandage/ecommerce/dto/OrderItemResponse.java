package com.bandage.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemResponse(
        Long id,
        String name,
        Integer count,
        BigDecimal price,
        String detail,
        List<ProductImageResponse> images
) {
}
