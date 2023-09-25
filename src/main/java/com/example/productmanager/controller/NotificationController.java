package com.example.productmanager.controller;


import com.example.productmanager.entity.Notification;
import com.example.productmanager.dao.NotificationRepository;
import com.example.productmanager.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public String showNotifications(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        List<Notification> notifications = notificationRepository.findAllByUserUserIdOrderByCreatedAtDesc(loggedInUser.userId);

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("notifications", notifications);
        return "notifications/list";
    }

    @GetMapping("/{id}")
    public String showNotificationDetail(@PathVariable Long id, Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setReadStatus(true);
            notificationRepository.save(notification);
            model.addAttribute("notification", notification);
            return "notifications/detail";
        } else {
            // Xử lý thông báo không tồn tại
            return "redirect:/notifications";
        }
    }
}
