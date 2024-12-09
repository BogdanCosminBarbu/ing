package com.ing.intrw.controller;

import com.ing.intrw.exception.InvalidRequestException;
import com.ing.intrw.exception.ProductNotFoundException;
import com.ing.intrw.model.Product;
import com.ing.intrw.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product) {
        log.info("Adding product: {}", product);
        Product addedProduct = productService.addProduct(product);
        log.info("Product added: {}", addedProduct);
        return addedProduct;
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productService.findProduct(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });
        log.info("Product found: {}", product);
        return product;
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Product> updatePrice(@PathVariable Long id, @RequestBody Map<String, Double> requestBody) {
        log.info("Updating price for product with ID: {}. New price: {}", id, requestBody.get("price"));
        Double newPrice = requestBody.get("price");
        if (newPrice == null) {
            log.error("Price is required for product with ID: {}", id);
            throw new InvalidRequestException("Price is required");
        }
        Product updatedProduct = productService.updatePrice(id, newPrice);
        if (updatedProduct != null) {
            log.info("Updated product price: {}", updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.info("Deleting product with ID: {}", id);
        boolean isDeleted = productService.deleteItemById(id);
        if (isDeleted) {
            log.info("Product with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("Product with ID: {} not found for deletion", id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> listAllItems() {
        List<Product> products = productService.listAllItems();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> filterItemsByCategory(@PathVariable String category) {
        List<Product> products = productService.filterItemsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> filterItemsByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Product> products = productService.filterItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sort/name")
    public ResponseEntity<List<Product>> sortItemsByName() {
        List<Product> products = productService.sortItemsByName();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sort/stock")
    public ResponseEntity<List<Product>> sortItemsByStockQuantity() {
        List<Product> products = productService.sortItemsByStockQuantity();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStockQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> requestBody) {
        Integer newQuantity = requestBody.get("stock");

        if (newQuantity == null || newQuantity < 0) {
            throw new InvalidRequestException("Stock quantity must be non-negative");
        }

        Product updatedProduct = productService.updateStockQuantity(id, newQuantity);

        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
}
