package com.example.productmanager.controller;


import com.example.productmanager.dao.MessageRepository;
import com.example.productmanager.entity.Message;
import com.example.productmanager.entity.User;
import com.example.productmanager.entity.UserInfo;
import com.example.productmanager.service.MessageService;
import com.example.productmanager.service.UserInfoService;
import com.example.productmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RequestMapping("/messages")
@Controller
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final UserInfoService userInfoService;
    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, UserInfoService userInfoService) {
        this.messageService = messageService;
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/chat/{otherUsername}")
    public String chatWithUser(@PathVariable String otherUsername, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        User otherUser = userService.findByUsername(otherUsername);

        if (otherUser == null) {
            return "messages/list";
        }
        UserInfo userInfo = userInfoService.getUserInfoByUserId(otherUser.getUserId());

        List<Message> messages = messageService.findMessagesBetweenUsers(loggedInUser.userId, otherUser.userId);

        if (messages != null) {
            for (Message message : messages) {
                if (!message.isStatus()) {
                    message.setStatus(true);
                    messageService.saveMessage(message);
                }
            }
        }




        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("messages", messages);
        model.addAttribute("otherUser", otherUser);
        return "messages/chat";

    }

    @PostMapping("/chat/{otherUsername}")
    public String sendChatMessage(@PathVariable String otherUsername, @RequestParam("messageText") String messageText, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        User otherUser = userService.findByUsername(otherUsername);

        if (loggedInUser != null && otherUser != null) {
            Message message = new Message();
            message.setUser1(loggedInUser);
            message.setUser2(otherUser);
            message.setText(messageText);
            message.setTimeChat(new Timestamp(System.currentTimeMillis()));
            message.setViolate(false);
            message.setStatus(false);
            messageService.saveMessage(message);
        } else {
            // Xử lý người dùng hoặc phiên làm việc không hợp lệ
            return "redirect:/users/login";
        }
        return "redirect:/messages/chat/" + otherUsername;
    }

    @GetMapping("/userList")
    public String userList(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        Set<User> displayedUsers = new HashSet<>(); // Tạo HashSet lưu người dùng đã hiển thị
        List<User> uniqueUsers = new ArrayList<>(); // Danh sách người dùng duy nhất

        List<Message> users = messageService.findMessagesByUser1(loggedInUser);
        for (Message user : users) {
            if (!displayedUsers.contains(user.getUser2())) {
                displayedUsers.add(user.getUser2());
                uniqueUsers.add(user.getUser2());
            }
        }


        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("users", uniqueUsers);

        return "messages/list";
    }

}
