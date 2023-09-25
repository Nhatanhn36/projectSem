package com.example.productmanager.dao;

import com.example.productmanager.entity.CartItem;
import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUserUserId(Long userId);

    List<CartItem> findByUserUserIdAndProductSellerUserId(Long userId, Long sellerId);

    List<CartItem> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    CartItem findByUserAndProduct(User loggedInUser, Product product);

    void deleteAllByIdIn(List<Long> cartItemIds);

}

