package com.example.inventory.module_14_assignment.service;

import com.example.inventory.module_14_assignment.exception.InvalidSkuFormatException;
import com.example.inventory.module_14_assignment.exception.ProductNotFoundException;
import com.example.inventory.module_14_assignment.exception.SkuAlreadyExistsException;
import com.example.inventory.module_14_assignment.model.Product;
import com.example.inventory.module_14_assignment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private static final Pattern SKU_PATTERN = Pattern.compile("^SKU-[A-Za-z0-9]{8}$");

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        log.debug("Received request to create product: {}", product);
        validateSkuFormat(product.getSku());
        ensureSkuUnique(product.getSku());

        Product saved = productRepository.save(product);
        log.info("Product created with ID: {} and SKU: {}", saved.getId(), saved.getSku());
        return saved;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products", products.size());
        return products;
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        log.info("Fetched product with ID: {}", id);
        return product;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        log.debug("Received request to update product with ID {}: {}", id, updatedProduct);

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        // SKU format is validated even during update; SKU value itself is immutable.
        validateSkuFormat(updatedProduct.getSku());
        if (!existing.getSku().equals(updatedProduct.getSku())) {
            throw new InvalidSkuFormatException("SKU cannot be changed for an existing product");
        }

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setStatus(updatedProduct.getStatus());

        Product saved = productRepository.save(existing);
        log.info("Updated product with ID: {}", id);
        return saved;
    }

    public void deleteProduct(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        productRepository.delete(existing);
        log.info("Deleted product with ID: {}", id);
    }

    private void validateSkuFormat(String sku) {
        if (sku == null || !SKU_PATTERN.matcher(sku).matches()) {
            throw new InvalidSkuFormatException("Invalid SKU format. Expected: SKU-XXXXXXXX (8 alphanumeric chars)");
        }
    }

    private void ensureSkuUnique(String sku) {
        if (productRepository.existsBySku(sku)) {
            throw new SkuAlreadyExistsException("SKU already exists: " + sku);
        }
    }
}

