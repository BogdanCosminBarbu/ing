package com.ing.intrw;

import com.ing.intrw.controller.ProductController;
import com.ing.intrw.model.Product;
import com.ing.intrw.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testAddProduct() throws Exception {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("{\"name\":\"Product A\",\"category\":\"Category A\",\"price\":100.0,\"stock\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void testFindProduct() throws Exception {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        when(productService.findProduct(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void testUpdatePrice() throws Exception {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        product.setPrice(150.0);
        when(productService.updatePrice(1L, 150.0)).thenReturn(product);
        mockMvc.perform(put("/api/products/{id}/price", 1)
                        .contentType("application/json")
                        .content("{\"price\":150.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(150.0));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(productService.deleteItemById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListAllItems() throws Exception {
        Product product1 = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        Product product2 = new Product(2L, "Product B", "Description", 20.0, 200, "Furniture", "A123");
        when(productService.listAllItems()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));
    }

    @Test
    void testFilterItemsByCategory() throws Exception {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        when(productService.filterItemsByCategory("Electronics")).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products/category/{category}", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Electronics"));
    }

    @Test
    void testFilterItemsByPriceRange() throws Exception {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        when(productService.filterItemsByPriceRange(50.0, 150.0)).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products/price?minPrice=50.0&maxPrice=150.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(10.0));
    }

    @Test
    void testSortItemsByName() throws Exception {
        Product product1 = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        Product product2 = new Product(2L, "Product B", "Description", 20.0, 200, "Furniture", "A123");
        when(productService.sortItemsByName()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products/sort/name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));
    }

    @Test
    void testUpdateStockQuantity() throws Exception {
        Product product = new Product("Product A", "Description", 10.0, 100, "Electronics", "A123");
        product.setStock(30);
        when(productService.updateStockQuantity(1L, 30)).thenReturn(product);
        mockMvc.perform(put("/api/products/{id}/stock", 1)
                        .contentType("application/json")
                        .content("{\"stock\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(30));
    }

}