package com.ing.intrw;

import com.ing.intrw.exception.ProductNotFoundException;
import com.ing.intrw.model.Product;
import com.ing.intrw.repository.ProductRepository;
import com.ing.intrw.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Product A", savedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testFindProduct() {
        Product product = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.findProduct(1L);

        assertTrue(foundProduct.isPresent());
        assertEquals("Product A", foundProduct.get().getName());
    }

    @Test
    void testUpdatePrice() {
        Product product = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updatePrice(1L, 20.0);

        assertNotNull(updatedProduct);
        assertEquals(20.0, updatedProduct.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteItemById() {
        Product product = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean isDeleted = productService.deleteItemById(1L);

        assertTrue(isDeleted);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteItemByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteItemById(1L));
    }

    @Test
    void testListAllItems() {
        Product product1 = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());
        Product product2 = new Product(2L, "Product B", "Description", 15.0, 200, "Furniture", "B456", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.listAllItems();

        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    void testFilterItemsByCategory() {
        Product product = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findByCategory("Electronics")).thenReturn(List.of(product));

        List<Product> products = productService.filterItemsByCategory("Electronics");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Electronics", products.get(0).getCategory());
    }

    @Test
    void testFilterItemsByPriceRange() {
        Product product = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findByPriceBetween(5.0, 20.0)).thenReturn(List.of(product));

        List<Product> products = productService.filterItemsByPriceRange(5.0, 20.0);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(10.0, products.get(0).getPrice());
    }

    @Test
    void testSortItemsByName() {
        Product product1 = new Product(1L, "Product B", "Description", 10.0, 100, "Electronics", "B123", LocalDateTime.now(), LocalDateTime.now());
        Product product2 = new Product(2L, "Product A", "Description", 20.0, 50, "Furniture", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findAll(Sort.by(Sort.Order.asc("name")))).thenReturn(List.of(product2, product1));

        List<Product> products = productService.sortItemsByName();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product A", products.get(0).getName());
    }

    @Test
    void testSortItemsByStockQuantity() {
        Product product1 = new Product(1L, "Product B", "Description", 10.0, 100, "Electronics", "B123", LocalDateTime.now(), LocalDateTime.now());
        Product product2 = new Product(2L, "Product A", "Description", 20.0, 200, "Furniture", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findAll(Sort.by(Sort.Order.asc("stock")))).thenReturn(List.of(product1, product2));

        List<Product> products = productService.sortItemsByStockQuantity();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(100, products.get(0).getStock());
    }

    @Test
    void testUpdateStockQuantity() {
        Product product = new Product(1L, "Product A", "Description", 10.0, 100, "Electronics", "A123", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateStockQuantity(1L, 150);

        assertNotNull(updatedProduct);
        assertEquals(150, updatedProduct.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
