package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.AddressRequest;
import com.bandage.ecommerce.dto.AddressResponse;
import com.bandage.ecommerce.dto.CreditCardRequest;
import com.bandage.ecommerce.dto.CreditCardResponse;
import com.bandage.ecommerce.entity.Address;
import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.entity.CreditCard;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.AddressRepository;
import com.bandage.ecommerce.repository.CreditCardRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDataService {
    private final AddressRepository addressRepository;
    private final CreditCardRepository creditCardRepository;

    public UserDataService(AddressRepository addressRepository, CreditCardRepository creditCardRepository) {
        this.addressRepository = addressRepository;
        this.creditCardRepository = creditCardRepository;
    }

    public List<AddressResponse> getAddresses(AppUser user) {
        return addressRepository.findByUserOrderByIdAsc(user).stream().map(DtoMapper::address).toList();
    }

    @Transactional
    public AddressResponse addAddress(AppUser user, AddressRequest request) {
        Address address = new Address();
        copyAddressFields(address, request);
        address.setUser(user);
        return DtoMapper.address(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse updateAddress(AppUser user, AddressRequest request) {
        if (request.id() == null) {
            throw new ApiException("Address id is required.", HttpStatus.BAD_REQUEST);
        }
        Address address = addressRepository.findByIdAndUser(request.id(), user)
                .orElseThrow(() -> new ApiException("Address not found.", HttpStatus.NOT_FOUND));
        copyAddressFields(address, request);
        return DtoMapper.address(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(AppUser user, Long addressId) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new ApiException("Address not found.", HttpStatus.NOT_FOUND));
        addressRepository.delete(address);
    }

    public List<CreditCardResponse> getCards(AppUser user) {
        return creditCardRepository.findByUserOrderByIdAsc(user).stream().map(DtoMapper::card).toList();
    }

    @Transactional
    public CreditCardResponse addCard(AppUser user, CreditCardRequest request) {
        CreditCard card = new CreditCard();
        copyCardFields(card, request);
        card.setUser(user);
        return DtoMapper.card(creditCardRepository.save(card));
    }

    @Transactional
    public CreditCardResponse updateCard(AppUser user, CreditCardRequest request) {
        if (request.id() == null) {
            throw new ApiException("Card id is required.", HttpStatus.BAD_REQUEST);
        }
        CreditCard card = creditCardRepository.findByIdAndUser(request.id(), user)
                .orElseThrow(() -> new ApiException("Card not found.", HttpStatus.NOT_FOUND));
        copyCardFields(card, request);
        return DtoMapper.card(creditCardRepository.save(card));
    }

    @Transactional
    public void deleteCard(AppUser user, Long cardId) {
        CreditCard card = creditCardRepository.findByIdAndUser(cardId, user)
                .orElseThrow(() -> new ApiException("Card not found.", HttpStatus.NOT_FOUND));
        creditCardRepository.delete(card);
    }

    private void copyAddressFields(Address address, AddressRequest request) {
        address.setTitle(request.title());
        address.setName(request.name());
        address.setSurname(request.surname());
        address.setPhone(request.phone());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setNeighborhood(request.neighborhood());
        address.setAddress(request.address());
    }

    private void copyCardFields(CreditCard card, CreditCardRequest request) {
        card.setCardNo(maskCardNo(request.cardNo()));
        card.setExpireMonth(request.expireMonth());
        card.setExpireYear(request.expireYear());
        card.setNameOnCard(request.nameOnCard());
    }

    private String maskCardNo(String cardNo) {
        String digits = cardNo == null ? "" : cardNo.replaceAll("\\D", "");
        if (digits.length() <= 4) {
            return digits;
        }
        return "************" + digits.substring(digits.length() - 4);
    }
}
