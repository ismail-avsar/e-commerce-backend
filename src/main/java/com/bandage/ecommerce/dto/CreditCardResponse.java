package com.bandage.ecommerce.dto;

public record CreditCardResponse(Long id, String cardNo, Integer expireMonth, Integer expireYear, String nameOnCard) {
}
