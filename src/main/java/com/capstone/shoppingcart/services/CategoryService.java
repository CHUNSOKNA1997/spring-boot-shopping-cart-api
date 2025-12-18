package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.CategoryResponseDto;
import com.capstone.shoppingcart.dtos.ProductResponseDto;
import com.capstone.shoppingcart.entities.Category;
import com.capstone.shoppingcart.mappers.CategoryMapper;
import com.capstone.shoppingcart.mappers.ProductMapper;
import com.capstone.shoppingcart.repositories.CategoryRepository;
import com.capstone.shoppingcart.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CategoryService(CategoryRepository categoryRepository,
                          CategoryMapper categoryMapper,
                          ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Get all categories
     * @return List of all categories
     */
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get category by ID
     * @param id - Category ID
     * @return Category details
     */
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return categoryMapper.toDto(category);
    }

    /**
     * Get products in a category with pagination
     * @param categoryId - Category ID
     * @param pageable - Pagination and sorting parameters
     * @return Page of products in the category
     */
    public Page<ProductResponseDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        // Verify category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        
        Page<com.capstone.shoppingcart.entities.Product> products = 
                productRepository.findByCategory_Id(categoryId, pageable);
        
        return products.map(productMapper::toDto);
    }
}
