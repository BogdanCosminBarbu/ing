package com.ing.intrw.service;

import com.ing.intrw.exception.ProductNotFoundException;
import com.ing.intrw.model.Product;
import com.ing.intrw.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Optional<Product> findProduct(Long id) {
        return productRepository.findById(id);
    }

    public Product updatePrice(Long id, Double newPrice) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setPrice(newPrice);
            product.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(product);
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    public boolean deleteItemById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    public List<Product> listAllItems() {
        return productRepository.findAll();
    }

    public List<Product> filterItemsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> filterItemsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> sortItemsByName() {
        return productRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    public List<Product> sortItemsByStockQuantity() {
        return productRepository.findAll(Sort.by(Sort.Order.asc("stock")));
    }

    public Product updateStockQuantity(Long id, Integer newQuantity) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStock(newQuantity);
            product.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(product);
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }
}
