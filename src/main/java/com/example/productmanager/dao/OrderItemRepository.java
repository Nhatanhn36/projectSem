package com.example.productmanager.dao;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderItem;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemsByOrderId(Order loggedInUser);


    List<OrderItem> findAll();

    List<OrderItem> findByOrderId(Order orderId);




}
