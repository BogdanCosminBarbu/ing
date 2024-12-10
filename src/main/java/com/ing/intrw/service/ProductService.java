package com.ing.intrw.service;

import com.ing.intrw.exception.NoProductsFoundException;
import com.ing.intrw.exception.ProductNotFoundException;
import com.ing.intrw.model.Product;
import com.ing.intrw.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        log.info("Saving product: {}", product);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        log.info("Product saved: {}", savedProduct);
        return savedProduct;
    }

    public Optional<Product> findProduct(Long id) {
        return productRepository.findById(id);
    }

    public Product updatePrice(Long id, Double newPrice) {
        log.info("Updating price for product with ID: {}. New price: {}", id, newPrice);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setPrice(newPrice);
            product.setUpdatedAt(LocalDateTime.now());
            Product updatedProduct = productRepository.save(product);
            log.info("Price updated for product with ID: {}", id);
            return updatedProduct;
        } else {
            log.error("Product not found with ID: {}", id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public boolean deleteItemById(Long id) {
        log.info("Attempting to delete product with ID: {}", id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            productRepository.deleteById(id);
            log.info("Product with ID: {} deleted successfully", id);
            return true;
        } else {
            log.error("Product not found with ID: {}", id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public List<Product> listAllItems() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.warn("No products found in the inventory");
            throw new NoProductsFoundException("No products found in the inventory.");
        }
        log.info("Fetched {} products", products.size());
        return products;
    }

    public List<Product> filterItemsByCategory(String category) {
        log.info("Filtering products by category: {}", category);
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) {
            log.warn("No products found in category: {}", category);
            throw new NoProductsFoundException("No products found in category: " + category);
        }
        log.info("Found {} products in category: {}", products.size(), category);
        return products;
    }

    public List<Product> filterItemsByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Filtering products by price range: {} - {}", minPrice, maxPrice);
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        if (products.isEmpty()) {
            log.warn("No products found in price range: {} - {}", minPrice, maxPrice);
            throw new NoProductsFoundException("No products found in price range: " + minPrice + " - " + maxPrice);
        }
        log.info("Found {} products in price range: {} - {}", products.size(), minPrice, maxPrice);
        return products;
    }

    public List<Product> sortItemsByName() {
        log.info("Sorting products by name");
        List<Product> products = productRepository.findAll(Sort.by(Sort.Order.asc("name")));
        if (products.isEmpty()) {
            log.warn("No products found to sort by name");
            throw new NoProductsFoundException("No products found to sort by name.");
        }
        log.info("Sorted {} products by name", products.size());
        return products;
    }

    public List<Product> sortItemsByStockQuantity() {
        log.info("Sorting products by stock quantity");
        List<Product> products = productRepository.findAll(Sort.by(Sort.Order.asc("stock")));
        if (products.isEmpty()) {
            log.warn("No products found to sort by stock quantity");
            throw new NoProductsFoundException("No products found to sort by stock quantity.");
        }
        log.info("Sorted {} products by stock quantity", products.size());
        return products;
    }

    public Product updateStockQuantity(Long id, Integer newQuantity) {
        log.info("Updating stock quantity for product with ID: {}. New quantity: {}", id, newQuantity);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStock(newQuantity);
            product.setUpdatedAt(LocalDateTime.now());
            Product updatedProduct = productRepository.save(product);
            log.info("Stock updated for product with ID: {}", id);
            return updatedProduct;
        } else {
            log.error("Product not found with ID: {}", id);
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }
}
