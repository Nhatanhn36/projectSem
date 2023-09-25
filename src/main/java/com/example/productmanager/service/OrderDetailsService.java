package com.example.productmanager.service;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderDetails;
import com.example.productmanager.entity.OrderItem;
import com.example.productmanager.entity.User;

import java.util.List;

public interface OrderDetailsService {
    void saveOrderDetails(OrderDetails orderDetails);

    OrderDetails getAllPaymentMethod();

    List<OrderDetails> getOrderDetailsByUser(Order loggedInUser);

    OrderDetails findByOrderId(Order orderId);
    // Khai báo thêm các phương thức cần thiết cho OrderDetailsService
}
