package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.OrderProductRequest;
import com.bandage.ecommerce.dto.OrderRequest;
import com.bandage.ecommerce.dto.OrderResponse;
import com.bandage.ecommerce.entity.Address;
import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.entity.CustomerOrder;
import com.bandage.ecommerce.entity.OrderItem;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.AddressRepository;
import com.bandage.ecommerce.repository.CustomerOrderRepository;
import com.bandage.ecommerce.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final CustomerOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    public OrderService(
            CustomerOrderRepository orderRepository,
            ProductRepository productRepository,
            AddressRepository addressRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(AppUser user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user).stream()
                .map(DtoMapper::order)
                .toList();
    }


    @Transactional
    public OrderResponse createOrder(AppUser user, OrderRequest request) {
        Address address = addressRepository.findByIdAndUser(request.addressId(), user)
                .orElseThrow(() -> new ApiException("Address not found.", HttpStatus.NOT_FOUND));

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(request.orderDate() == null ? LocalDateTime.now() : request.orderDate());
        order.setCardNo(request.cardNo());
        order.setCardName(request.cardName());
        order.setCardExpireMonth(request.cardExpireMonth());
        order.setCardExpireYear(request.cardExpireYear());
        order.setPrice(request.price());

        for (OrderProductRequest productRequest : request.products()) {
            Product product = productRepository.findById(productRequest.productId())
                    .orElseThrow(() -> new ApiException("Product not found.", HttpStatus.NOT_FOUND));
            int count = productRequest.count() == null ? 1 : productRequest.count();
            if (product.getStock() != null && product.getStock() < count) {
                throw new ApiException(product.getName() + " has not enough stock.", HttpStatus.BAD_REQUEST);
            }
            if (product.getStock() != null) {
                product.setStock(product.getStock() - count);
            }
            product.setSellCount((product.getSellCount() == null ? 0 : product.getSellCount()) + count);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setCount(count);
            item.setDetail(productRequest.detail());
            item.setUnitPrice(product.getPrice());
            order.addItem(item);
        }

        return DtoMapper.order(orderRepository.save(order));
    }
}
