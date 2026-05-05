package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.CategoryResponse;
import com.bandage.ecommerce.dto.ProductListResponse;
import com.bandage.ecommerce.dto.ProductResponse;
import com.bandage.ecommerce.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/categories")
    public List<CategoryResponse> categories() {
        return productService.getCategories();
    }

    @GetMapping("/products")
    public ProductListResponse products(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "25") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset
    ) {
        return productService.getProducts(category, filter, sort, limit, offset);
    }

    @GetMapping("/products/{productId}")
    public ProductResponse product(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }
}
