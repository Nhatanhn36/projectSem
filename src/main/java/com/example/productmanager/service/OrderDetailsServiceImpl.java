package com.example.productmanager.service;

import com.example.productmanager.dao.OrderDetailsRepository;
import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderDetails;
import com.example.productmanager.dao.OrderDetailsRepository;
import com.example.productmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public void saveOrderDetails(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }

    @Override
    public OrderDetails getAllPaymentMethod() {
       return orderDetailsRepository.getAllPaymentMethod();
    }

    @Override
    public List<OrderDetails> getOrderDetailsByUser(Order loggedInUser) {
        return orderDetailsRepository.getOrderDetailsByOrderId(loggedInUser);
    }

    @Override
    public OrderDetails findByOrderId(Order orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }

//    @Override
//    public OrderDetails findByOrderId(Long orderId) {
//        return orderDetailsRepository.findByOrderId(orderId);
//    }
}
