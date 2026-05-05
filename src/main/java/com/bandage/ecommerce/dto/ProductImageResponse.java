package com.bandage.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductImageResponse(String url, @JsonProperty("index") Integer index) {
}
