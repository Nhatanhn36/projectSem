package com.example.productmanager.controller;


import com.example.productmanager.update.VipExpirationScheduler;
import com.example.productmanager.dao.NotificationRepository;
import com.example.productmanager.dao.UserRepository;
import com.example.productmanager.entity.User;
import com.example.productmanager.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpSession;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final VipExpirationScheduler vipExpirationScheduler;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public UserController(UserService userService, VipExpirationScheduler vipExpirationScheduler,
                        UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.vipExpirationScheduler = vipExpirationScheduler;

        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }




//=================================================================================================
@PostMapping("/generateQrCode")
public String generateQrCode(@RequestParam("amount") int amount, HttpSession session, Model model) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");

//    String stk = "0383087656";
    String stk = "0520134188007";
    String addInfo = "TOPUP%20" + amount + "%20" + loggedInUser.username;
    String accountName = "SHOPPLAZA";
    String imageUrl = "https://img.vietqr.io/image/" +
            "mbbank-"+stk+"-compact2.jpg?" +
            "amount="+ amount +"" +
            "&addInfo="+ addInfo +
            "&accountName=" + accountName;

    // Sử dụng thư viện để tạo mã QR code từ URL hình ảnh
    ByteArrayOutputStream qrCodeStream = QRCodeGenerator.generateQRCodeImage(imageUrl, 200, 200);

    byte[] qrCodeImageBytes = qrCodeStream.toByteArray();
    String qrCodeImageBase64 = Base64.getEncoder().encodeToString(qrCodeImageBytes);

    model.addAttribute("qrCodeImage", qrCodeImageBase64);
    return "payment/qrcode";
}




    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("amount") double amount, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // Thực hiện cập nhật số tiền trong tài khoản của người dùng
            // ...
            System.out.println("ok");

            // Gửi thông báo cho người dùng đã thực hiện thanh toán thành công
            messagingTemplate.convertAndSend("/topic/payment", "Payment successful");
        }
        return "redirect:/home"; // Hoặc trang nào khác tương ứng
    }



    @GetMapping("/topup")
    public String showTopUpPage(HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null){
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);

        return "payment/top-up";
    }

    //=================================================================================================



    @GetMapping("/list")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list-users";
    }
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("userId") Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "users/user-form";
    }





    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteById(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null){
            return "redirect:/home";
        }
        User user = new User();
        model.addAttribute("user", user);
        return "users/login-form";
    }
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user,
             Model model, HttpSession session) {

        // Xác thực người dùng và lấy thông tin từ cơ sở dữ liệu
        User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            session.setAttribute("loggedInUser", authenticatedUser);
            if (authenticatedUser.ban){
                model.addAttribute("error", "Người dùng đã bị khóa");
                return "users/login-form";
            }
            vipExpirationScheduler.setLoggedInUser(authenticatedUser);
//            orderStatusScheduler.setLoggedInUser(authenticatedUser); // Đặt thông tin loggedInUser

            // Lưu thông tin người dùng vào phiên làm việc
            session.setAttribute("loggedInUser", authenticatedUser);
            UserRole userRole = UserRole.values()[authenticatedUser.getRole()];

            System.out.println(session + "/ " + authenticatedUser.username +"/ " + authenticatedUser.userId + " /" + userRole + " /" + authenticatedUser.ban);
            if (userRole == UserRole.ADMIN) {
                return "redirect:/admin/home";
            } else if (userRole == UserRole.SELLER) {
                return "redirect:/seller/home";
            } else {
                return "redirect:/home";
            }
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "users/login-form";
        }
    }

    public enum UserRole {
        USER(0), SELLER(1), ADMIN(2);

        private final int value;

        UserRole(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa thông tin người dùng khỏi phiên làm việc
        session.removeAttribute("loggedInUser");
        return "redirect:/users/login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "users/registration-form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Kiểm tra xem người dùng đã tồn tại chưa
        if (userService.findByUsername(user.getUsername()) != null) {
            // Xử lý lỗi: người dùng đã tồn tại
            return "redirect:/users/register?error";
        }

        // Mã hóa mật khẩu trước khi lưu vào đối tượng User
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);

        // Lưu đối tượng User vào cơ sở dữ liệu
        userService.save(user);

        return "redirect:/users/login";
    }

    private String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
