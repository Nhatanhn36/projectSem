package com.example.productmanager.controller;

import com.example.productmanager.entity.Category;
import com.example.productmanager.entity.User;
import com.example.productmanager.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/header")
    public String header(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model
        return "fragments/header";
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("loggedInUser", loggedInUser);
        return "home";
    }


}
