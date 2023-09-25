package com.example.productmanager.controller;

    import com.example.productmanager.dao.NotificationRepository;
    import com.example.productmanager.dao.VIPByUserRepository;
    import com.example.productmanager.entity.*;
    import com.example.productmanager.service.ImageService;
    import com.example.productmanager.service.UserInfoService;
    import com.example.productmanager.service.VipService;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.sql.Timestamp;
    import java.util.List;

@Controller
@RequestMapping("/user-info")
public class UserInfoController {

        private final UserInfoService userInfoService;
        private final ImageService imageService;
        private final VipService vipService;
        private final VIPByUserRepository vipByUserRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
        public UserInfoController(UserInfoService userInfoService, ImageService imageService, VipService vipService, VIPByUserRepository vipByUserRepository, NotificationRepository notificationRepository) {
            this.userInfoService = userInfoService;
            this.imageService = imageService;
            this.vipService = vipService;
            this.vipByUserRepository = vipByUserRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/edit")
    public String editUserInfo(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        List<VIPByUser> vip = vipByUserRepository.findVipTimeLeftByUserUserId(loggedInUser.getUserId());
        model.addAttribute("vcl", vip);
        model.addAttribute("loggedInUser", loggedInUser);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(loggedInUser.getUserId());
        model.addAttribute("userInfo", userInfo);
        return "user-info/edit-user-info-form";
    }


        @GetMapping("/vip")
        public String listVip(Model model) {
            List<VIPPackage> vip = vipService.findAll();
            model.addAttribute("vip", vip);
            return "users/vip-package";
        }


        @PostMapping("/create-user-info")
        public String createUserInfo(
                @RequestParam("userId") Long userId,
                @RequestParam("imageAvatar") MultipartFile imageAvatar,
                @RequestParam("fullname") String fullname,
                @RequestParam("email") String email,
                @RequestParam("phone") String phone,
                @RequestParam("gender") String gender,
                @RequestParam("date") String date,
                @RequestParam("address") String address,
                HttpSession session
        ) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                return "redirect:/users/login"; // Redirect to login if not logged in
            }

        UserInfo userInfo = new UserInfo();
        userInfo.setUser(loggedInUser);

        if (imageAvatar != null && !imageAvatar.isEmpty()) {
            try {
                // Upload and store the new image
                String newImageFileName = imageService.uploadImage(imageAvatar);
                userInfo.setImageAvatar(newImageFileName);
            } catch (IOException e) {
                // Handle image upload error
                e.printStackTrace();
            }
        }

        // Set other fields
        userInfo.setUser(loggedInUser);
        userInfo.setFullname(fullname);
        userInfo.setEmail(email);
        userInfo.setPhone(phone);
        userInfo.setGender(gender);
        userInfo.setDate(String.valueOf(date));
        userInfo.setAddress(address);


        userInfoService.saveUserInfo(userInfo);







            Notification notification = new Notification();
            notification.setUser(loggedInUser);
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            notification.setTitle("Cập nhật thông tin thành công");
            notification.setContent("Xin chào " + loggedInUser.username);
            notification.setNote("Thông tin của bạn sẽ được dùng trên hệ thống ShopPlaza,vui lòng xác nhận rằng đây là thông tin thật của bạn");
            notification.setReadStatus(false);
            notificationRepository.save(notification);


        return "redirect:/user-info/edit";
    }


    @GetMapping("/create")
    public String getInfo(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        // Kiểm tra xem đã tồn tại thông tin người dùng cho userId này chưa
        UserInfo existingUserInfo = userInfoService.getUserInfoByUserId(loggedInUser.getUserId());
        if (existingUserInfo != null) {
            return "redirect:/user-info/edit";
        }

        // Nếu chưa có, tạo thông tin người dùng mới với userId và lưu vào model
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUser(loggedInUser);
        model.addAttribute("userInfo", newUserInfo);
        model.addAttribute("loggedInUser", loggedInUser);
        return "user-info/create-user-info-form";
    }

    @PostMapping("/update")
    public String updateUserInfo(
            @RequestParam("userId") Long userId,
            @RequestParam("imageAvatar") MultipartFile imageAvatar,
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam("date") String dateString, // Thay đổi tên biến thành dateString
            @RequestParam("address") String address,
            HttpSession session
    ) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }

        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);

        if (imageAvatar != null && !imageAvatar.isEmpty()) {
            try {
                // Nếu có thay đổi ảnh mới, tải lên và lưu tên tệp ảnh mới
                String newImageFileName = imageService.uploadImage(imageAvatar);
                userInfo.setImageAvatar(newImageFileName);
            } catch (IOException e) {
                // Handle image upload error
                e.printStackTrace();
            }
        }

        // Update other fields
        userInfo.setFullname(fullname);
        userInfo.setEmail(email);
        userInfo.setPhone(phone);
        userInfo.setGender(gender);

            userInfo.setDate((dateString)); // Chuyển đổi chuỗi thành ngày

        userInfo.setAddress(address);

        userInfoService.saveUserInfo(userInfo);

        return "redirect:/user-info/edit";
    }
}
