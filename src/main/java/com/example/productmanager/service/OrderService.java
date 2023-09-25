package com.example.productmanager.service;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.User;

import java.util.List;

public interface OrderService {
    void saveOrder(Order order);

    Order getOrderById(Long orderId);

    boolean orderExistsWithCode(String code);

    List<Order> getOrdersByUser(User loggedInUser);

    List<Order> getOrdersByUserAndStatus(User loggedInUser, String statusFilter);

   Order findById(Order orderId);

    Order findById(Long orderId);

    List<Order> getOrdersBySeller(User loggedInUser);

    List<Order> getOrdersBySellerAndStatus(User loggedInUser, String status);


    List<Order> getOrdersByStatus(String delivered);
}
