package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.AddressResponse;
import com.bandage.ecommerce.dto.CategoryResponse;
import com.bandage.ecommerce.dto.CreditCardResponse;
import com.bandage.ecommerce.dto.OrderItemResponse;
import com.bandage.ecommerce.dto.OrderResponse;
import com.bandage.ecommerce.dto.ProductImageResponse;
import com.bandage.ecommerce.dto.ProductResponse;
import com.bandage.ecommerce.dto.RoleResponse;
import com.bandage.ecommerce.entity.Address;
import com.bandage.ecommerce.entity.Category;
import com.bandage.ecommerce.entity.CreditCard;
import com.bandage.ecommerce.entity.CustomerOrder;
import com.bandage.ecommerce.entity.OrderItem;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.entity.ProductImage;
import com.bandage.ecommerce.entity.Role;
import java.util.Comparator;
import java.util.List;

public final class DtoMapper {
    private DtoMapper() {
    }

    public static RoleResponse role(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getCode());
    }

    public static CategoryResponse category(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCode(),
                category.getTitle(),
                category.getGender(),
                category.getRating(),
                category.getImageUrl()
        );
    }

    public static ProductResponse product(Product product) {
        Long storeId = product.getStore() == null ? null : product.getStore().getId();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                storeId,
                product.getCategory().getId(),
                product.getRating(),
                product.getSellCount(),
                product.getImages().stream()
                        .sorted(Comparator.comparing(ProductImage::getImageIndex))
                        .map(image -> new ProductImageResponse(image.getUrl(), image.getImageIndex()))
                        .toList()
        );
    }

    public static AddressResponse address(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getTitle(),
                address.getName(),
                address.getSurname(),
                address.getPhone(),
                address.getCity(),
                address.getDistrict(),
                address.getNeighborhood(),
                address.getAddress()
        );
    }

    public static CreditCardResponse card(CreditCard card) {
        return new CreditCardResponse(
                card.getId(),
                card.getCardNo(),
                card.getExpireMonth(),
                card.getExpireYear(),
                card.getNameOnCard()
        );
    }

    public static OrderResponse order(CustomerOrder order) {
        List<OrderItemResponse> products = order.getProducts().stream()
                .map(DtoMapper::orderItem)
                .toList();
        return new OrderResponse(order.getId(), order.getOrderDate(), order.getPrice(), order.getCardNo(), products);
    }

    private static OrderItemResponse orderItem(OrderItem item) {
        Product product = item.getProduct();
        return new OrderItemResponse(
                product.getId(),
                product.getName(),
                item.getCount(),
                item.getUnitPrice(),
                item.getDetail(),
                product.getImages().stream()
                        .sorted(Comparator.comparing(ProductImage::getImageIndex))
                        .map(image -> new ProductImageResponse(image.getUrl(), image.getImageIndex()))
                        .toList()
        );
    }
}
