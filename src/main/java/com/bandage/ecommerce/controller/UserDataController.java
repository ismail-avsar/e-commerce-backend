package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.AddressRequest;
import com.bandage.ecommerce.dto.AddressResponse;
import com.bandage.ecommerce.dto.CreditCardRequest;
import com.bandage.ecommerce.dto.CreditCardResponse;
import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.service.CurrentUserService;
import com.bandage.ecommerce.service.UserDataService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDataController {
    private final CurrentUserService currentUserService;
    private final UserDataService userDataService;

    public UserDataController(CurrentUserService currentUserService, UserDataService userDataService) {
        this.currentUserService = currentUserService;
        this.userDataService = userDataService;
    }

    @GetMapping("/user/address")
    public List<AddressResponse> addresses() {
        return userDataService.getAddresses(currentUser());
    }

    @PostMapping("/user/address")
    public AddressResponse addAddress(@Valid @RequestBody AddressRequest request) {
        return userDataService.addAddress(currentUser(), request);
    }

    @PutMapping("/user/address")
    public AddressResponse updateAddress(@Valid @RequestBody AddressRequest request) {
        return userDataService.updateAddress(currentUser(), request);
    }

    @DeleteMapping("/user/address/{addressId}")
    public Map<String, Boolean> deleteAddress(@PathVariable Long addressId) {
        userDataService.deleteAddress(currentUser(), addressId);
        return Map.of("success", true);
    }

    @GetMapping("/user/card")
    public List<CreditCardResponse> cards() {
        return userDataService.getCards(currentUser());
    }

    @PostMapping("/user/card")
    public CreditCardResponse addCard(@Valid @RequestBody CreditCardRequest request) {
        return userDataService.addCard(currentUser(), request);
    }

    @PutMapping("/user/card")
    public CreditCardResponse updateCard(@Valid @RequestBody CreditCardRequest request) {
        return userDataService.updateCard(currentUser(), request);
    }

    @DeleteMapping("/user/card/{cardId}")
    public Map<String, Boolean> deleteCard(@PathVariable Long cardId) {
        userDataService.deleteCard(currentUser(), cardId);
        return Map.of("success", true);
    }

    private AppUser currentUser() {
        return currentUserService.getCurrentUser();
    }
}
