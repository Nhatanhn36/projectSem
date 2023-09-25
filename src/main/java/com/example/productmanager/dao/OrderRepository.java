package com.example.productmanager.dao;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Order getByOrderId(Long orderId);

    boolean existsByCode(String code);


    List<Order> findByUserAndStatus(User user, String status);

    List<Order> findByUser(User user);

    List<Order> findBySeller(User user);

    List<Order> findBySellerAndStatus(User loggedInUser, String status);

    List<Order> findByStatus(String status);
}