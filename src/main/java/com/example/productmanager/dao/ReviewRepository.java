package com.example.productmanager.dao;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.Review;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_ProductIdOrderByCreatedAtDesc(Long productId);

    List<Review> findByProduct_ProductId(Long productId);

    List<Review> findByUserAndProductAndOrder(User user, Product product, Order order);

    List<Review> findByUser_UserId(long loggedInUser);

}
