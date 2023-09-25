package com.example.productmanager.service;

import com.example.productmanager.entity.Order;
import com.example.productmanager.dao.OrderRepository;
import com.example.productmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getByOrderId(orderId);
    }

    @Override
    public boolean orderExistsWithCode(String code) {
        return orderRepository.existsByCode(code);
    }


    @Override
    public List<Order> getOrdersByUserAndStatus(User user, String status) {
        if (status != null && !status.isEmpty()) {
            return orderRepository.findByUserAndStatus(user, status);
        } else {
            return orderRepository.findByUser(user);
        }
    }

    @Override
    public Order findById(Order orderId) {
        return orderRepository.findById(orderId.getOrderId()).orElse(null);
    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> getOrdersBySeller(User loggedInUser) {
        return orderRepository.findBySeller(loggedInUser);
    }

    @Override
    public List<Order> getOrdersBySellerAndStatus(User loggedInUser, String status) {
        if (status != null && !status.isEmpty()) {
            return orderRepository.findBySellerAndStatus(loggedInUser, status);
        } else {
            return orderRepository.findByUser(loggedInUser);
        }
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }


    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

}
