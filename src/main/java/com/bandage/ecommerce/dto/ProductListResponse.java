package com.bandage.ecommerce.dto;

import java.util.List;

public record ProductListResponse(long total, List<ProductResponse> products) {
}
