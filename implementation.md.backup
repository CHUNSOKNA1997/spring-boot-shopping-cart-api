# Remaining Implementation Guide

## Shopping Cart API - Complete E-Commerce System

**Last Updated:** 2025-12-24  
**Current Status:** Customer Portal Complete + Wishlist ✅ | Role System ✅

---

## 📊 Implementation Status

### ✅ Completed Features

| Feature | Status | Endpoints |
|---------|--------|-----------|
| Authentication | ✅ Complete | `POST /api/customer/v1/auth/signup`, `POST /api/customer/v1/auth/signin` |
| Cart Management | ✅ Complete | `GET /api/customer/v1/cart`, `POST /api/customer/v1/cart/items`, `PUT /api/customer/v1/cart/items/{id}`, `DELETE /api/customer/v1/cart/items/{id}` |
| Products | ✅ Complete | `GET /api/customer/v1/products`, `GET /api/customer/v1/products/{id}` |
| Categories | ✅ Complete | `GET /api/customer/v1/categories`, `GET /api/customer/v1/categories/{id}`, `GET /api/customer/v1/categories/{id}/products` |
| Profile | ✅ Complete | `GET /api/customer/v1/profile`, `PUT /api/customer/v1/profile`, `PUT /api/customer/v1/profile/password` |
| Addresses | ✅ Complete | `GET /api/customer/v1/addresses`, `POST /api/customer/v1/addresses`, `PUT /api/customer/v1/addresses/{id}`, `DELETE /api/customer/v1/addresses/{id}` |
| **Wishlist** | ✅ Complete | `GET /api/customer/v1/wishlist`, `POST /api/customer/v1/wishlist/products/{id}`, `DELETE /api/customer/v1/wishlist/products/{id}`, `DELETE /api/customer/v1/wishlist` |
| **Role System** | ✅ Complete | UserRole enum (CUSTOMER, ADMIN), Role-based security configured |

### 🔄 Remaining Features

**Phase 1: Admin Dashboard** (Step 3-5 Remaining)
- ⚠️ Step 1-2 Complete: Role system & Security configuration
- 🔲 Step 3: Admin Product Management
- 🔲 Step 4: Admin Category Management  
- 🔲 Step 5: Admin User Management (Optional)

**Phase 2: Orders & Checkout** (Critical - Not Started)
- 🔲 Order Entity & DTOs
- 🔲 Checkout Process
- 🔲 Order History
- 🔲 Order Tracking

**Phase 3: Payment Integration** (Not Started)
- 🔲 Stripe/PayPal Integration

**Phase 4: Reviews & Ratings** (Optional - Not Started)

---

## ✅ 1️⃣ Wishlist Management - COMPLETE

**Status:** ✅ Complete  
**Completed:** 2025-12-24  
**Time Taken:** ~45 minutes

### What Was Built

✅ **Files Created:**
1. `WishListResponseDto.java` - Response DTO with product list
2. `WishListRepository.java` - Database queries  
3. `WishListService.java` - Business logic with JavaDoc
4. `WishListController.java` - REST endpoints

✅ **API Endpoints:**
- `GET /api/customer/v1/wishlist` - Get user's wishlist
- `POST /api/customer/v1/wishlist/products/{productId}` - Add product to wishlist
- `DELETE /api/customer/v1/wishlist/products/{productId}` - Remove product from wishlist
- `DELETE /api/customer/v1/wishlist` - Clear entire wishlist

✅ **Features:**
- Auto-creates wishlist if doesn't exist
- Prevents duplicate products
- Uses MapStruct for entity-to-DTO mapping
- Full JavaDoc documentation

**Note:** Implementation uses simpler ManyToMany relationship instead of OneToMany with WishListItem entity.

---

## 🔄 2️⃣ Admin Dashboard - IN PROGRESS

**Priority:** High  
**Complexity:** ⭐⭐⭐ Complex  
**Estimated Time:** ~2-3 hours  
**Current Progress:** Step 1-2 Complete (Role System & Security)

### ✅ Completed Steps

#### Step 1: Role System ✅
**Files Modified:**
- `UserRole.java` - Enum with CUSTOMER and ADMIN roles
- `User.java` - Added role field with default CUSTOMER
- `UserDetailsServiceImpl.java` - Updated to include role authorities

**What was done:**
```java
@Enumerated(EnumType.STRING)
@Column(name = "role", nullable = false)
private UserRole role = UserRole.CUSTOMER;
```

#### Step 2: Security Configuration ✅
**Files Modified:**
- `SecurityConfig.java` - Added role-based access control

**What was configured:**
```java
// Admin endpoints - require ADMIN role
.requestMatchers("/api/admin/v1/**").hasRole("ADMIN")

// Customer endpoints - require authentication (any role)
.requestMatchers("/api/customer/v1/**").authenticated()
```

**Security Rules:**
- Public: `/api/customer/v1/auth/**`, `/api/customer/v1/products/**`, `/api/customer/v1/categories/**`
- Admin Only: `/api/admin/v1/**` - Returns 403 if not ADMIN
- Customer: `/api/customer/v1/**` - Requires login (any role)

### 🔲 Remaining Steps

#### Step 3: Admin Product Management (NOT STARTED)
    public ResponseEntity<String> clearWishList(@AuthenticationPrincipal UserDetails userDetails);
}
```

#### Step 6: Update SecurityConfig

All wishlist endpoints require authentication (already protected by default).

### API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/customer/v1/wishlist` | Get my wishlist | ✅ Yes |
| POST | `/api/customer/v1/wishlist/items` | Add product to wishlist | ✅ Yes |
| DELETE | `/api/customer/v1/wishlist/items/{id}` | Remove item by ID | ✅ Yes |
| DELETE | `/api/customer/v1/wishlist/products/{productId}` | Remove product | ✅ Yes |
| DELETE | `/api/customer/v1/wishlist` | Clear wishlist | ✅ Yes |

### Example Requests

**Get Wishlist:**
```bash
GET /api/customer/v1/wishlist
Authorization: Bearer <JWT_TOKEN>
```

**Add to Wishlist:**
```bash
POST /api/customer/v1/wishlist/items
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "productId": 1
}
```

**Remove from Wishlist:**
```bash
DELETE /api/customer/v1/wishlist/items/{itemId}
Authorization: Bearer <JWT_TOKEN>
```

---

## 2️⃣ Admin Dashboard

**Priority:** High  
**Complexity:** ⭐⭐⭐ Complex  
**Estimated Time:** ~2-3 hours

### Overview
Admin panel for managing products, categories, and users.

### Implementation Steps

#### Step 1: Create Admin Role System

**Update User Entity:**
```java
@Entity
@Table(name = "users")
public class User {
    // ... existing fields
    
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CUSTOMER;
}
```

**UserRole.java** (enum)
```java
package com.capstone.shoppingcart.enums;

public enum UserRole {
    CUSTOMER,
    ADMIN
}
```

#### Step 2: Update Security Configuration

**SecurityConfig.java:**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            // Public endpoints
            .requestMatchers("/api/customer/v1/auth/**").permitAll()
            .requestMatchers("/api/customer/v1/products/**").permitAll()
            .requestMatchers("/api/customer/v1/categories/**").permitAll()
            
            // Admin endpoints (require ADMIN role)
            .requestMatchers("/api/admin/v1/**").hasRole("ADMIN")
            
            // All other customer endpoints require authentication
            .anyRequest().authenticated()
        )
        // ... rest of configuration
}
```

#### Step 3: Create Admin DTOs

**CreateProductRequest.java**
```java
package com.capstone.shoppingcart.dtos;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double price;
    
    @NotNull(message = "Stock is required")
    @Min(value = 0)
    private Integer stock;
    
    private String imageUrl;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
```

**UpdateProductRequest.java**
```java
package com.capstone.shoppingcart.dtos;

@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
}
```

**CreateCategoryRequest.java**
```java
package com.capstone.shoppingcart.dtos;

@Data
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
}
```

**UpdateCategoryRequest.java**
```java
package com.capstone.shoppingcart.dtos;

@Data
public class UpdateCategoryRequest {
    private String name;
    private String description;
}
```

#### Step 4: Create Admin Services

**AdminProductService.java**
```java
package com.capstone.shoppingcart.services.admin;

@Service
public class AdminProductService {
    
    /**
     * Create new product
     * @param request - Product data
     * @return Created product
     */
    @Transactional
    public ProductResponseDto createProduct(CreateProductRequest request);
    
    /**
     * Update existing product
     * @param productId - Product ID
     * @param request - Updated data
     * @return Updated product
     */
    @Transactional
    public ProductResponseDto updateProduct(Long productId, UpdateProductRequest request);
    
    /**
     * Delete product
     * @param productId - Product ID to delete
     */
    @Transactional
    public void deleteProduct(Long productId);
    
    /**
     * Get all products with pagination (admin view - includes stock info)
     * @param pageable - Pagination parameters
     * @return Page of products
     */
    public Page<ProductResponseDto> getAllProducts(Pageable pageable);
}
```

**AdminCategoryService.java**
```java
package com.capstone.shoppingcart.services.admin;

@Service
public class AdminCategoryService {
    
    /**
     * Create new category
     * @param request - Category data
     * @return Created category
     */
    @Transactional
    public CategoryResponseDto createCategory(CreateCategoryRequest request);
    
    /**
     * Update existing category
     * @param categoryId - Category ID
     * @param request - Updated data
     * @return Updated category
     */
    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, UpdateCategoryRequest request);
    
    /**
     * Delete category
     * @param categoryId - Category ID to delete
     */
    @Transactional
    public void deleteCategory(Long categoryId);
}
```

#### Step 5: Create Admin Controllers

**AdminProductController.java**
```java
package com.capstone.shoppingcart.controllers.admin;

@RestController
@RequestMapping("/api/admin/v1/products")
public class AdminProductController {
    
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable);
    
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductRequest request);
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProductRequest request);
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id);
}
```

**AdminCategoryController.java**
```java
package com.capstone.shoppingcart.controllers.admin;

@RestController
@RequestMapping("/api/admin/v1/categories")
public class AdminCategoryController {
    
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CreateCategoryRequest request);
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody UpdateCategoryRequest request);
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id);
}
```

**AdminUserController.java** (Optional)
```java
package com.capstone.shoppingcart.controllers.admin;

@RestController
@RequestMapping("/api/admin/v1/users")
public class AdminUserController {
    
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable);
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id);
    
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
        @PathVariable UUID id,
        @RequestBody UpdateUserRoleRequest request);
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id);
}
```

### Admin API Endpoints Summary

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| **Products** |
| GET | `/api/admin/v1/products` | List all products | ADMIN |
| POST | `/api/admin/v1/products` | Create product | ADMIN |
| PUT | `/api/admin/v1/products/{id}` | Update product | ADMIN |
| DELETE | `/api/admin/v1/products/{id}` | Delete product | ADMIN |
| **Categories** |
| POST | `/api/admin/v1/categories` | Create category | ADMIN |
| PUT | `/api/admin/v1/categories/{id}` | Update category | ADMIN |
| DELETE | `/api/admin/v1/categories/{id}` | Delete category | ADMIN |
| **Users** |
| GET | `/api/admin/v1/users` | List all users | ADMIN |
| GET | `/api/admin/v1/users/{id}` | Get user details | ADMIN |
| PUT | `/api/admin/v1/users/{id}/role` | Change user role | ADMIN |
| DELETE | `/api/admin/v1/users/{id}` | Delete user | ADMIN |

### Key Considerations

1. **Security:**
   - Only ADMIN role can access `/api/admin/v1/**`
   - Validate admin status on every request
   - Log all admin actions (audit trail)

2. **Business Rules:**
   - Cannot delete category with products (add check or cascade delete)
   - Cannot delete user with active orders (if orders implemented)
   - Stock management validation

3. **Testing:**
   - Create admin user manually in database first
   - Test role-based access control

---

## 3️⃣ Order Management (Optional - Future)

**Priority:** Low (Future Enhancement)  
**Complexity:** ⭐⭐⭐⭐ Very Complex  
**Estimated Time:** ~4-6 hours

### Overview
Complete checkout and order management system.

### Features to Implement

1. **Checkout Process**
   - Convert cart to order
   - Payment integration (Stripe/PayPal)
   - Order confirmation

2. **Order Management**
   - View order history
   - Order details
   - Order status tracking
   - Cancel order

3. **Admin Order Management**
   - View all orders
   - Update order status
   - Order analytics

### Entities Needed

- `Order` (orderId, user, total, status, createdAt)
- `OrderItem` (orderItemId, order, product, quantity, price)
- `Payment` (paymentId, order, amount, method, status)

### Status Flow
```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
                  ↓
              CANCELLED
```

---

## 📝 Implementation Checklist

### Before Starting
- [ ] Review existing code structure
- [ ] Set up test environment
- [ ] Create test data (products, categories)
- [ ] Create admin user in database

### Wishlist Implementation
- [ ] Create DTOs (WishListResponseDto, WishListItemDto, AddToWishListRequest)
- [ ] Create Repositories (WishListRepository, WishListItemRepository)
- [ ] Create Mapper (WishListMapper)
- [ ] Create Service (WishListService with JavaDoc)
- [ ] Create Controller (WishListController)
- [ ] Test endpoints with Postman
- [ ] Verify business logic (no duplicates, auto-create)

### Admin Dashboard Implementation
- [ ] Create UserRole enum
- [ ] Update User entity with role field
- [ ] Create migration script for existing users
- [ ] Update SecurityConfig with role-based access
- [ ] Create Admin DTOs (CreateProductRequest, UpdateProductRequest, etc.)
- [ ] Create Admin Services (AdminProductService, AdminCategoryService)
- [ ] Create Admin Controllers (AdminProductController, AdminCategoryController)
- [ ] Test role-based access control
- [ ] Create admin user and test all endpoints

---

## 🚀 Quick Start Commands

### Create Admin User (SQL)
```sql
-- Update existing user to admin
UPDATE users 
SET role = 'ADMIN' 
WHERE email = 'admin@example.com';

-- Or create new admin user
INSERT INTO users (user_id, username, email, password, role, created_at)
VALUES (
    gen_random_uuid(),
    'admin',
    'admin@example.com',
    '$2a$10$...', -- Use BCrypt hashed password
    'ADMIN',
    NOW()
);
```

### Test Endpoints
```bash
# Customer endpoints (require auth)
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/customer/v1/wishlist

# Admin endpoints (require ADMIN role)
curl -H "Authorization: Bearer <ADMIN_TOKEN>" http://localhost:8080/api/admin/v1/products
```

---

## 📚 Additional Resources

### Best Practices Followed
- ✅ Constructor injection (not @Autowired)
- ✅ JavaDoc comments on service methods
- ✅ DTO pattern for API responses
- ✅ MapStruct for entity-DTO conversion
- ✅ @Transactional for write operations
- ✅ Security checks (findByIdAndUser pattern)
- ✅ Validation annotations (@Valid, @NotNull, etc.)
- ✅ Builder pattern with Lombok
- ✅ RESTful API design

### Code Standards
- Package structure: `controllers/customer`, `controllers/admin`
- Service layer with business logic
- Repository layer for data access
- Clear separation of concerns
- Consistent naming conventions

---

## 🎯 Next Steps

1. **Start with Wishlist** - Simpler, good warm-up
2. **Then Admin Dashboard** - More complex, builds on existing knowledge
3. **Optional: Orders** - Advanced feature for later

**Recommended Order:**
```
Wishlist (30 min) → Admin Dashboard (2-3 hours) → Break/Review → Orders (Future)
```

---

## 📞 Support

If you encounter issues:
1. Check the existing code structure
2. Review completed features for patterns
3. Verify database schema matches entities
4. Test endpoints incrementally
5. Review error messages carefully

---

**Good luck with the implementation! 🚀**

*Last updated: 2025-12-18*
