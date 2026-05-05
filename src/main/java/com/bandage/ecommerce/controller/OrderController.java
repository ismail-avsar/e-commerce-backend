package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.OrderRequest;
import com.bandage.ecommerce.dto.OrderResponse;
import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.service.CurrentUserService;
import com.bandage.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final CurrentUserService currentUserService;
    private final OrderService orderService;

    public OrderController(CurrentUserService currentUserService, OrderService orderService) {
        this.currentUserService = currentUserService;
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public List<OrderResponse> orders() {
        return orderService.getOrders(currentUser());
    }

    @PostMapping("/order")
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.createOrder(currentUser(), request);
    }

    private AppUser currentUser() {
        return currentUserService.getCurrentUser();
    }
}
