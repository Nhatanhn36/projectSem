package com.example.productmanager.service;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    void saveOrderItem(OrderItem orderItem);

    List<OrderItem> getOrderItemsByUser(Order loggedInUser);

    List<OrderItem> getAllOrderItems();
    List<OrderItem> findByOrderId(Order orderId);



}
