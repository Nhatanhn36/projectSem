package com.example.productmanager.controller;

import com.example.productmanager.dao.NotificationRepository;
import com.example.productmanager.dao.UserRepository;
import com.example.productmanager.dao.VIPByUserRepository;
import com.example.productmanager.dao.VIPPackageRepository;
import com.example.productmanager.entity.*;
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

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/vip")
public class VIPController {

    private final VIPByUserRepository vipByUserRepository;
    private final VIPPackageRepository vipPackageRepository;
    private final VipService vipService;
    private final UserRepository userRepository;

    private final long ONE_DAY = 86400000L;
    private final long ONE_HOUR = ONE_DAY/24;
    private final long ONE_MINUTES = ONE_HOUR/60;
    private final UserInfoService userInfoService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public VIPController(VIPByUserRepository vipByUserRepository, VIPPackageRepository vipPackageRepository, VipService vipService, UserRepository userRepository, UserInfoService userInfoService, NotificationRepository notificationRepository) {
        this.vipByUserRepository = vipByUserRepository;
        this.vipPackageRepository = vipPackageRepository;
        this.vipService = vipService;
        this.userRepository = userRepository;
        this.userInfoService = userInfoService;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/buy")
    public String showBuyVIPPage(Model model, HttpSession session) {
        // Lấy thông tin người dùng đã đăng nhập từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        // Lấy danh sách các gói VIP từ cơ sở dữ liệu
        List<VIPPackage> vip = vipPackageRepository.findAll();
        model.addAttribute("vip", vip);
        model.addAttribute("loggedInUser", loggedInUser);
        return "vip/buy-vip";
    }

    @PostMapping("/buy")
    public String buyVIP(@RequestParam("vipPackageId") Long vipPackageId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        VIPPackage vipPackage = vipPackageRepository.findById(vipPackageId).orElse(null);
        if (vipPackage == null) {
            return "redirect:/vip/buy";
        }
        VIPByUser vipByUser = new VIPByUser();
        vipByUser.setUser(loggedInUser);
        vipByUser.setVipPackage(vipPackage);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(currentTime.getTime() + vipPackage.days * ONE_DAY );

        vipByUser.setCreateTime(currentTime);
        vipByUser.setEndTime(endTime);
        vipByUser.getUser().setVip((byte) vipByUser.getVipPackage().vipId);
        vipByUser.setStatus("ONLINE");

        if (vipByUser.getUser().getCoint() < vipPackage.getPrice()){
            return "redirect:/vip/buy";
        }
        vipByUser.getUser().coint -= vipPackage.getPrice();
        vipByUserRepository.save(vipByUser); // Lưu entity VIPByUser
        userRepository.save(loggedInUser); // Lưu entity loggedInUser sau khi cập nhật

        Notification notification = new Notification();
        notification.setUser(loggedInUser);
        notification.setCreatedAt(currentTime);
        notification.setTitle("Mua VIP thành công!");
        notification.setContent("Chúc mừng "+ loggedInUser.username +" đã đạt VIP" + vipPackage.vipId);
        notification.setNote("Bạn đã mua thành công gói [VIP"+ vipPackage.vipId + "] vào lúc:" + currentTime
                +" .Khi mua gói VIP bạn sẽ được hưởng các ưu đãi như ưu tiên hiển thị với những người dưới VIP"+vipPackage.vipId+"," +
                " và phí sàn trên mỗi sản phẩm sẽ được giảm "+vipPackage.percentOff+"% trên mỗi đơn hàng." +
                "Ưu đãi của bạn sẽ hết vào lúc: " + endTime + " (" + vipPackage.days +" Ngày).Sau khi hết hạn" +
                "VIP,các ưu đãi của bạn cũng sẽ mất và trở về như ban đầu.");
        notification.setReadStatus(false);
        notificationRepository.save(notification);
        return "redirect:/user-info/create";
    }


}
