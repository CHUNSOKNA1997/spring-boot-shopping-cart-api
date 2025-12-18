package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.ProductResponseDto;
import com.capstone.shoppingcart.entities.Product;
import com.capstone.shoppingcart.mappers.ProductMapper;
import com.capstone.shoppingcart.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Get all products with pagination
     * @param pageable - Pagination and sorting parameters
     * @return Page of products
     */
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toDto);
    }

    /**
     * Get product by ID
     * @param id - Product ID
     * @return Product details
     */
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return productMapper.toDto(product);
    }

    /**
     * Get products by category with pagination
     * @param categoryId - Category ID to filter by
     * @param pageable - Pagination and sorting parameters
     * @return Page of products in the category
     */
    public Page<ProductResponseDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByCategory_Id(categoryId, pageable);
        return products.map(productMapper::toDto);
    }

    /**
     * Search products by name or description
     * @param search - Search keyword
     * @param pageable - Pagination and sorting parameters
     * @return Page of matching products
     */
    public Page<ProductResponseDto> searchProducts(String search, Pageable pageable) {
        Page<Product> products = productRepository.searchProducts(search, pageable);
        return products.map(productMapper::toDto);
    }

    /**
     * Search products by category and keyword
     * @param categoryId - Category ID to filter by
     * @param search - Search keyword
     * @param pageable - Pagination and sorting parameters
     * @return Page of matching products in the category
     */
    public Page<ProductResponseDto> searchProductsByCategory(Long categoryId, String search, Pageable pageable) {
        Page<Product> products = productRepository.searchProductsByCategory(categoryId, search, pageable);
        return products.map(productMapper::toDto);
    }
}
