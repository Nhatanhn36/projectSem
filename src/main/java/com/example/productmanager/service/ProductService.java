package com.example.productmanager.service;

import com.example.productmanager.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<Product> findProductsBySeller(Long sellerId, Boolean hide); // Thêm phương thức này
    List<Product> findZeroQuantityProducts();

    Product findById(int theId);
    void save(Product theProduct);
    void deleteById(int theId);
    List<Product> findByHideOrderByCreatedAtDesc(Boolean hide);
    List<Product> findAvailableProducts();
    List<Product> findAvailableProductsBySeller(Long sellerId);
    List<Product> findHiddenProducts();
    List<Product> findByQuantityStock(int quantity);

    List<Product> findHiddenProductsBySeller(Long userId);

    List<Product> findByHideOrderByCreatedAtDesc();

    List<Product> findZeroQuantityProductsBySeller(Long userId);

    List<Product> findAvailableProductsByName(String query);

    List<Product> findAvailableProductsByCategoryId(Long categoryId);

}
