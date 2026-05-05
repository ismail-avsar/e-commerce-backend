package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.CategoryResponse;
import com.bandage.ecommerce.dto.ProductListResponse;
import com.bandage.ecommerce.dto.ProductResponse;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.CategoryRepository;
import com.bandage.ecommerce.repository.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(DtoMapper::category)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductListResponse getProducts(Long category, String filter, String sort, Integer limit, Integer offset) {
        int safeLimit = limit == null || limit < 1 ? 25 : limit;
        int safeOffset = offset == null || offset < 0 ? 0 : offset;

        List<Product> filtered = productRepository.findAll().stream()
                .filter(product -> category == null || product.getCategory().getId().equals(category))
                .filter(product -> matchesFilter(product, filter))
                .sorted(comparator(sort))
                .toList();

        List<ProductResponse> products = filtered.stream()
                .skip(safeOffset)
                .limit(safeLimit)
                .map(DtoMapper::product)
                .toList();

        return new ProductListResponse(filtered.size(), products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Product not found.", HttpStatus.NOT_FOUND));
        return DtoMapper.product(product);
    }

    private boolean matchesFilter(Product product, String filter) {
        if (filter == null || filter.isBlank()) {
            return true;
        }
        String loweredFilter = filter.toLowerCase();
        return product.getName().toLowerCase().contains(loweredFilter)
                || (product.getDescription() != null && product.getDescription().toLowerCase().contains(loweredFilter));
    }

    private Comparator<Product> comparator(String sort) {
        Comparator<Product> byId = Comparator.comparing(Product::getId);
        if (sort == null || sort.isBlank()) {
            return byId;
        }
        return switch (sort) {
            case "price:asc" -> Comparator.comparing(Product::getPrice).thenComparing(Product::getId);
            case "price:desc" -> Comparator.comparing(Product::getPrice).reversed().thenComparing(Product::getId);
            case "rating:asc" -> Comparator.comparing(Product::getRating).thenComparing(Product::getId);
            case "rating:desc" -> Comparator.comparing(Product::getRating).reversed().thenComparing(Product::getId);
            default -> byId;
        };
    }
}
