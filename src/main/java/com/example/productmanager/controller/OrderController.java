package com.example.productmanager.controller;

import com.example.productmanager.dao.NotificationRepository;
import com.example.productmanager.dao.UserRepository;
import com.example.productmanager.entity.*;
import com.example.productmanager.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService; // Inject OrderService
    private final OrderItemService orderItemService; // Inject OrderService
    private final OrderDetailsService orderDetailsService; // Inject OrderService
    private final UserService userService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService, OrderDetailsService orderDetailsService, UserService userService, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.orderDetailsService = orderDetailsService;
        this.userService = userService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public String listOrders(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

       if (loggedInUser.role == 0){
           List<Order> orders = orderService.getOrdersByUser(loggedInUser);
           model.addAttribute("orders", orders);
       }else {
           List<Order> orders = orderService.getOrdersBySeller(loggedInUser);
           model.addAttribute("orders", orders);
       }

        model.addAttribute("loggedInUser", loggedInUser);

        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        model.addAttribute("orderItems", orderItems);

        return "orders/list-orders";
    }

    @GetMapping("/list/{status}")
    public String listOrdersByStatus(@PathVariable String status, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        if (loggedInUser.role == 0){
            List<Order> orders = orderService.getOrdersByUserAndStatus(loggedInUser, status);
            model.addAttribute("orders", orders);
        }else {
            List<Order> orders = orderService.getOrdersBySellerAndStatus(loggedInUser, status);
            model.addAttribute("orders", orders);
        }
        model.addAttribute("loggedInUser", loggedInUser);

        return "orders/list-orders";
    }


    @PostMapping("/updateStatus/{orderId}/{currentStatus}")
    public String updateOrderStatus(@PathVariable Order orderId, @PathVariable String currentStatus,HttpSession session) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "redirect:/orders/list";
        }
        List<String> orderStatusList = Arrays.asList("PENDING", "SHIPPING", "DELIVERED", "SUCCESS", "CANCEL", "REFUND");
        int currentIndex = orderStatusList.indexOf(currentStatus);
        if (currentIndex == -1) {
            return "redirect:/orders/list";
        }
        int nextIndex = currentIndex + 1;
        if (nextIndex >= orderStatusList.size()) {
            // Xử lý khi không có trạng thái tiếp theo
            return "redirect:/orders/list";
        }
        String newStatus = orderStatusList.get(nextIndex);

        order.setStatus(newStatus);
        order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        orderService.saveOrder(order);

        if (newStatus.equals("SUCCESS")) {
            double totalAmount = order.getTotalAmount();
            double percent = 10 - order.getSeller().vip;
            double orderCoints = totalAmount - (totalAmount * percent / 100);//tiền nhận
            Notification notification = new Notification();
            notification.setUser(order.getSeller());
            notification.setContent("Thông Báo hoàn thành đơn hàng");
            notification.setTitle("Đơn hàng số " + order.getOrderId() + " có mã tracking là " + order.getCode() + " đã được người dùng xác nhận thành công");
            notification.setNote("Đơn hàng của bạn đã được xác nhận thành công," +
                    " bạn nhận được " + orderCoints + " Coints (đã trừ chiết khấu " + percent + "%)");
            notification.setReadStatus(false);
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            notificationRepository.save(notification);

            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                return "redirect:/users/login";
            }
            order.getSeller().setCoint(order.getSeller().getCoint() + orderCoints);
                userService.save(order.getSeller());
        }



        // Redirect lại trang danh sách đơn hàng
        return "redirect:/orders/list/" + newStatus;
    }









    @GetMapping("/details/{orderId}")
    public String showOrderDetails(@PathVariable Order orderId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }

        Order order = orderService.findById(orderId);
        if (order == null) {
            // Handle case when order doesn't exist
            return "redirect:/orders/list";
        }

        // Check if the logged-in user is authorized to view this order
        if (!order.getUser().getUserId().equals(loggedInUser.getUserId()) &&
                !order.getSeller().getUserId().equals(loggedInUser.getUserId())) {
            // Handle unauthorized access to order details
            return "redirect:/orders/list";
        }

        List<OrderItem> orderItems = orderItemService.findByOrderId(order);
        OrderDetails orderDetails = orderDetailsService.findByOrderId(order);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("loggedInUser", loggedInUser);

        return "orders/order-details";
    }

}
