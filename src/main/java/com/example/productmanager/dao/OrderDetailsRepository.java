package com.example.productmanager.dao;

import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderDetails;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    @Query("SELECT u.paymentMethod FROM OrderDetails u")
    OrderDetails getAllPaymentMethod();

    List<OrderDetails> getOrderDetailsByOrderId(Order loggedInUser);

    OrderDetails findByOrderId(Order orderId);


//    @Query("SELECT u FROM OrderDetails u where u.orderId = :orderId")
//    OrderDetails findByOrderId(Long orderId);

}
