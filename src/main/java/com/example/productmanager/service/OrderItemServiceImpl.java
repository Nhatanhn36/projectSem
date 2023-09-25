package com.example.productmanager.service;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderItem;
import com.example.productmanager.dao.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getOrderItemsByUser(Order loggedInUser) {
        return orderItemRepository.getOrderItemsByOrderId(loggedInUser);
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public List<OrderItem> findByOrderId(Order orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }


}
