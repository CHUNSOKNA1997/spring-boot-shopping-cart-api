package com.capstone.shoppingcart.config;

import com.capstone.shoppingcart.entities.Category;
import com.capstone.shoppingcart.entities.Product;
import com.capstone.shoppingcart.repositories.CategoryRepository;
import com.capstone.shoppingcart.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            log.info("Data seeding skipped: app.seed.enabled=false");
            return;
        }

        Map<String, Category> categories = seedCategories();
        int insertedProducts = seedProducts(categories);

        log.info(
                "Data seeding finished. categoriesAvailable={}, productsInserted={}, totalProducts={}",
                categories.size(),
                insertedProducts,
                productRepository.count()
        );
    }

    private Map<String, Category> seedCategories() {
        List<CategorySeed> categorySeeds = List.of(
                new CategorySeed("Electronics", "Smart devices and gadgets"),
                new CategorySeed("Fashion", "Clothing, shoes, and accessories"),
                new CategorySeed("Home & Kitchen", "Home essentials and kitchen tools"),
                new CategorySeed("Books", "Fiction, non-fiction, and learning books")
        );

        Map<String, Category> categoryMap = new HashMap<>();
        for (CategorySeed seed : categorySeeds) {
            Category category = categoryRepository.findByName(seed.name())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(seed.name());
                        newCategory.setDescription(seed.description());
                        newCategory.setCreatedAt(LocalDateTime.now());
                        return categoryRepository.save(newCategory);
                    });

            categoryMap.put(seed.name(), category);
        }

        return categoryMap;
    }

    private int seedProducts(Map<String, Category> categories) {
        List<ProductSeed> productSeeds = List.of(
                new ProductSeed("Wireless Mouse", "Ergonomic Bluetooth mouse", 29.99, 120, "https://picsum.photos/seed/mouse/640/480", "Electronics"),
                new ProductSeed("Mechanical Keyboard", "RGB backlit gaming keyboard", 89.99, 80, "https://picsum.photos/seed/keyboard/640/480", "Electronics"),
                new ProductSeed("Noise Cancelling Headphones", "Over-ear headphones with ANC", 149.99, 45, "https://picsum.photos/seed/headphones/640/480", "Electronics"),
                new ProductSeed("Cotton T-Shirt", "Soft cotton unisex t-shirt", 19.99, 200, "https://picsum.photos/seed/tshirt/640/480", "Fashion"),
                new ProductSeed("Running Sneakers", "Lightweight everyday running shoes", 79.99, 60, "https://picsum.photos/seed/sneakers/640/480", "Fashion"),
                new ProductSeed("Classic Denim Jacket", "Slim fit denim jacket", 64.99, 35, "https://picsum.photos/seed/jacket/640/480", "Fashion"),
                new ProductSeed("Ceramic Dinner Set", "12-piece ceramic dinnerware set", 54.99, 40, "https://picsum.photos/seed/dinner/640/480", "Home & Kitchen"),
                new ProductSeed("Blender Pro 900", "900W multi-speed blender", 99.99, 30, "https://picsum.photos/seed/blender/640/480", "Home & Kitchen"),
                new ProductSeed("Memory Foam Pillow", "Cooling memory foam sleeping pillow", 34.99, 90, "https://picsum.photos/seed/pillow/640/480", "Home & Kitchen"),
                new ProductSeed("Clean Code", "A Handbook of Agile Software Craftsmanship", 39.99, 75, "https://picsum.photos/seed/cleancode/640/480", "Books"),
                new ProductSeed("The Pragmatic Programmer", "Your Journey to Mastery", 42.99, 55, "https://picsum.photos/seed/pragmatic/640/480", "Books"),
                new ProductSeed("Atomic Habits", "An Easy and Proven Way to Build Good Habits", 24.99, 110, "https://picsum.photos/seed/habits/640/480", "Books")
        );

        int insertedCount = 0;
        for (ProductSeed seed : productSeeds) {
            boolean exists = productRepository.findByNameIgnoreCase(seed.name()).isPresent();
            if (exists) {
                continue;
            }

            Category category = categories.get(seed.categoryName());
            if (category == null) {
                throw new IllegalStateException("Category not found during seed: " + seed.categoryName());
            }

            Product product = Product.builder()
                    .name(seed.name())
                    .description(seed.description())
                    .price(seed.price())
                    .stock(seed.stock())
                    .imageUrl(seed.imageUrl())
                    .createdAt(LocalDateTime.now())
                    .category(category)
                    .build();

            productRepository.save(product);
            insertedCount++;
        }

        return insertedCount;
    }

    private record CategorySeed(String name, String description) {
    }

    private record ProductSeed(
            String name,
            String description,
            Double price,
            Integer stock,
            String imageUrl,
            String categoryName
    ) {
    }
}
