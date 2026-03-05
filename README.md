# Product Inventory API (Module 14 Assignment)

Simple Spring Boot REST API for managing products with validation, custom exceptions, and logging.

## Implemented Features

- Product entity with Bean Validation and custom messages
- ProductStatus enum: `ACTIVE`, `INACTIVE`, `DISCONTINUED`
- CRUD endpoints:
  - `POST /products`
  - `GET /products`
  - `GET /products/{id}`
  - `PUT /products/{id}`
  - `DELETE /products/{id}`
- SKU rules:
  - Format: `SKU-` + 8 alphanumeric characters
  - Must be unique
  - Cannot be changed on update
- Global exception handling with JSON error responses
- Logging with `@Slf4j` across controller, service, and exception handler

## Run

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

H2 console: `http://localhost:8080/h2-console`

# Ostad_Java_SpringBoot_3_Module_14_Assignment
