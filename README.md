# Spring Boot Shopping Cart API

A RESTful shopping cart API built with **Spring Boot 4**, **PostgreSQL**, and **JWT-based authentication**. The application supports both customer-facing and admin operations, including product browsing, cart management, wishlists, user profiles, addresses, and promotional banners.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Data Seeding](#data-seeding)
- [Running Tests](#running-tests)

---

## Features

- **Authentication** — JWT-based signup and signin with BCrypt password hashing
- **Products** — Browse, search, and filter products with pagination
- **Categories** — Browse categories and their associated products
- **Shopping Cart** — Add, update, and remove items; view cart totals
- **Wishlist** — Save and manage favorite products
- **User Profile** — Update personal details and change password
- **Shipping Addresses** — Manage multiple shipping addresses per user
- **Promotions** — View promotional banners/campaigns
- **Admin Panel** — CRUD operations for products and promotions (admin role required)
- **Role-based Access Control** — `CUSTOMER` and `ADMIN` roles enforced at the endpoint level
- **Global Exception Handling** — Centralised error responses

---

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Language |
| Spring Boot | 4.0.0 | Application framework |
| Spring Security | 4.0.0 | Authentication & authorisation |
| Spring Data JPA | 4.0.0 | ORM / database access |
| PostgreSQL | Latest | Relational database |
| JJWT | 0.12.3 | JWT generation & validation |
| MapStruct | 1.5.5.Final | DTO ↔ entity mapping |
| Lombok | Latest | Boilerplate code generation |
| Spring Dotenv | 4.0.0 | Environment variable management |
| Maven | 3.11.0 | Build & dependency management |

---

## Prerequisites

- **Java 17+**
- **Maven 3.8+** (or use the included `./mvnw` wrapper)
- **PostgreSQL 13+** — a running instance with a database created

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/CHUNSOKNA1997/spring-boot-shopping-cart-api.git
cd spring-boot-shopping-cart-api
```

### 2. Set up the configuration file

```bash
cp src/main/resources/application.example.yml src/main/resources/application.yml
```

Edit `src/main/resources/application.yml` and fill in your database credentials and JWT secret (see [Configuration](#configuration)).

### 3. Create a PostgreSQL database

```sql
CREATE DATABASE shopping_cart;
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The server will start on **http://localhost:8080** by default.  
Hibernate will automatically create/update the schema on first run (`ddl-auto: update`).

---

## Configuration

All configuration lives in `src/main/resources/application.yml` (git-ignored). Use `application.example.yml` as a template.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shopping_cart
    username: your_db_username
    password: your_db_password
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update        # use 'validate' in production
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

jwt:
  secret: your_jwt_secret_key_here_at_least_256_bits   # min 32 characters
  expiration: 86400000                                   # 24 hours in ms

app:
  seed:
    enabled: true   # set to false to skip startup data seeding
```

### Generating a secure JWT secret

```bash
openssl rand -base64 32
```

---

## API Endpoints

### Authentication (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/customer/v1/auth/signup` | Register a new user |
| `POST` | `/api/customer/v1/auth/signin` | Sign in and receive a JWT token |

### Products (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/products` | List products (paginated, searchable) |
| `GET` | `/api/customer/v1/products/{id}` | Get a single product |

Query parameters for listing: `categoryId`, `search`, `page` (default `0`), `size` (default `10`), `sortBy` (default `id`), `sortDir` (`asc`/`desc`).

### Categories (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/categories` | List all categories |
| `GET` | `/api/customer/v1/categories/{id}` | Get a category |
| `GET` | `/api/customer/v1/categories/{id}/products` | List products in a category (paginated) |

### Promotions (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/promotions` | List active promotions |
| `GET` | `/api/customer/v1/promotions/{id}` | Get a promotion |

### Shopping Cart (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/cart` | Get the current user's cart |
| `POST` | `/api/customer/v1/cart/items` | Add an item (`{ productId, quantity }`) |
| `PUT` | `/api/customer/v1/cart/items/{itemId}` | Update item quantity (`{ quantity }`) |
| `DELETE` | `/api/customer/v1/cart/items/{itemId}` | Remove an item |

### Profile (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/profile` | Get the current user's profile |
| `PUT` | `/api/customer/v1/profile` | Update profile (`{ firstName, lastName, phoneNumber }`) |
| `PUT` | `/api/customer/v1/profile/password` | Change password (`{ currentPassword, newPassword, confirmPassword }`) |

### Addresses (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/addresses` | List all addresses |
| `POST` | `/api/customer/v1/addresses` | Create an address |
| `PUT` | `/api/customer/v1/addresses/{id}` | Update an address |
| `DELETE` | `/api/customer/v1/addresses/{id}` | Delete an address |

### Wishlist (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/customer/v1/wishlist` | Get the current user's wishlist |
| `POST` | `/api/customer/v1/wishlist/products/{productId}` | Add a product to the wishlist |
| `DELETE` | `/api/customer/v1/wishlist/products/{productId}` | Remove a product from the wishlist |
| `DELETE` | `/api/customer/v1/wishlist` | Clear the entire wishlist |

### Admin — Products (admin role required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/v1/products` | List all products (paginated) |
| `POST` | `/api/admin/v1/products` | Create a product |
| `PUT` | `/api/admin/v1/products/{id}` | Update a product |
| `DELETE` | `/api/admin/v1/products/{id}` | Delete a product |

### Admin — Promotions (admin role required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/v1/promotions` | List all promotions (paginated) |
| `POST` | `/api/admin/v1/promotions` | Create a promotion |
| `PUT` | `/api/admin/v1/promotions/{id}` | Update a promotion |
| `DELETE` | `/api/admin/v1/promotions/{id}` | Delete a promotion |

#### Authentication header

All authenticated endpoints require a `Bearer` token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

---

## Project Structure

```
src/main/java/com/capstone/shoppingcart/
├── Application.java                  # Spring Boot entry point
├── config/
│   └── DataSeeder.java               # Startup data seeding
├── controllers/
│   ├── customer/                     # Customer-facing REST controllers
│   └── admin/                        # Admin REST controllers
├── services/
│   ├── auth/                         # Authentication & JWT services
│   ├── admin/                        # Admin business logic
│   └── *.java                        # Core business services
├── entities/                         # JPA entities (User, Product, Cart, …)
├── repositories/                     # Spring Data JPA repositories
├── dtos/                             # Request/response DTOs
├── mappers/                          # MapStruct entity ↔ DTO mappers
├── security/                         # Spring Security config & JWT filter
├── exceptions/                       # Global exception handler
└── enums/
    └── UserRole.java                 # CUSTOMER, ADMIN
```

---

## Data Seeding

When `app.seed.enabled: true` (the default), the application seeds the database with sample data on startup:

- **4 Categories** — Electronics, Fashion, Home & Kitchen, Books
- **12 Products** — spread across the categories above

Set `app.seed.enabled: false` in `application.yml` to disable this behaviour.

---

## Running Tests

```bash
./mvnw test
```
