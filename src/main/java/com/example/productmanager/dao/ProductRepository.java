package com.example.productmanager.dao;

import com.example.productmanager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findAllByOrderByPriceAsc();  // Sắp xếp theo giá sản phẩm tăng dần
    List<Product> findByHideOrderByCreatedAtDesc(Boolean hide);
    List<Product> findByQuantityStock(int quantity); // Thêm phương thức truy vấn theo quantity

    List<Product> findByQuantityStockGreaterThanAndHideAndSellerUserId(int quantity, boolean hide, Long sellerId);
    List<Product> findByQuantityStockGreaterThanAndHideOrderByCreatedAtDesc(int quantity, Boolean hide); // Tìm sản phẩm theo quantity > 0, hide = 0 và sắp xếp theo thời gian
    List<Product> findByHideAndSellerUserId(boolean hide, Long sellerId);

    List<Product> findByQuantityStockAndSellerUserId(int i, Long userId);

    List<Product> findBySellerUserIdAndHideOrderByCreatedAtDesc(Long sellerId, Boolean hide);

    List<Product> findBySellerUserIdAndQuantityStockGreaterThanAndHideOrderByCreatedAtDesc(Long sellerId, int i, boolean b);

    List<Product> findBySellerUserIdAndQuantityStockEquals(Long userId, int i);


    List<Product> findByCategoryIdAndQuantityStockGreaterThanAndHideFalse(Long categoryId, int i);

    List<Product> findByNameContainingIgnoreCaseAndQuantityStockGreaterThanAndHideFalse(String query, int i);
}