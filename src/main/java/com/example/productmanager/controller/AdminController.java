package com.example.productmanager.controller;

import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.User;
import com.example.productmanager.service.ProductService;
import com.example.productmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public AdminController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    //------------------------------------------------------------------------------
    @GetMapping("/home")
    public String adminHome(HttpSession session,Model model) {
        // Lấy thông tin người dùng từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }
        if (loggedInUser.role != 2) {
            return "home"; // Redirect with error message
        }

        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model

        return "admin/home";
    }
    //------------------------------------------------------------------------------

    @GetMapping("/seller-list")
    public String showSellerList(HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model

        List<User> sellers = userService.findSellers();
    model.addAttribute("sellers", sellers);
        return "admin/seller-list";
    }
    //------------------------------------------------------------------------------























    //=======================ACCOUNT=======================================================
    @GetMapping("/account")
    public String showAccountInfo(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", loggedInUser);
        return "admin/account";
    }
    //=======================PRODUCT=======================================================

}
