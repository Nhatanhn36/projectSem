package com.example.productmanager.update;

import com.example.productmanager.dao.NotificationRepository;
import com.example.productmanager.dao.UserInfoRepository;
import com.example.productmanager.dao.UserRepository;
import com.example.productmanager.dao.VIPByUserRepository;
import com.example.productmanager.entity.Notification;
import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.VIPByUser;
import com.example.productmanager.entity.User;
import com.example.productmanager.service.OrderService;
import com.example.productmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class VipExpirationScheduler {
    private final long timeUpdate = 360000;//60p
    private final VIPByUserRepository vipByUserRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderService orderService;

    private final UserInfoRepository userInfoRepository;
    private User loggedInUser; // Biến toàn cục
    private final NotificationRepository notificationRepository;

    @Autowired
    public VipExpirationScheduler(VIPByUserRepository vipByUserRepository, UserRepository userRepository, UserService userService, OrderService orderService, UserInfoRepository userInfoRepository, NotificationRepository notificationRepository) {
        this.vipByUserRepository = vipByUserRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.userInfoRepository = userInfoRepository;
        this.notificationRepository = notificationRepository;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
//    @Scheduled(cron = "0 * * * * *") // Chạy vào mỗi phút
//@Scheduled(cron = "*/30 * * * * *")//MỖI 30S
@Scheduled(cron = "* * * * * *")//MỖI S
public void checkVipExpiration() {
        if (loggedInUser == null) {
            return;
        }
        List<VIPByUser> vipByUsers = vipByUserRepository.findAll();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        for (VIPByUser vipByUser : vipByUsers) {
            if (vipByUser.getEndTime().before(currentTime)
                    || !vipByUser.getStatus().equals("ONLINE"))
            {//khi hết hạn hoặc trạng thái != online sẽ bị xóa
                loggedInUser.setVip((byte) 0);
                System.out.println("Hết hạn");
                vipByUserRepository.delete(vipByUser);
                userRepository.save(loggedInUser);
            }
        }
    //------------------------------------------------------------------------------ (1)
    if (loggedInUser != null){
        int roleInDB = userRepository.findRoleByUserId(loggedInUser.userId);
        double cointFromDatabase = userRepository.findCointById(loggedInUser.userId);
        int vipFromDatabase = userRepository.findVipIdById(loggedInUser.userId);
        String imgfromDB = userInfoRepository.findImageAvatarById(loggedInUser.userId);

        loggedInUser.setCoint(cointFromDatabase);
        loggedInUser.setVip((byte) vipFromDatabase);
        loggedInUser.setRole((byte) roleInDB);


        if (loggedInUser.getUserInfo() != null ){
            loggedInUser.getUserInfo().setImageAvatar(imgfromDB);
            userInfoRepository.save(loggedInUser.getUserInfo());
        }


        List<Order> deliveredOrders = orderService.getOrdersByStatus("DELIVERED");

        for (Order order : deliveredOrders) {
            Timestamp updateTime = order.getUpdateTime();
            Timestamp currentTimes = new Timestamp(System.currentTimeMillis());

            long timeDifference = currentTimes.getTime() - updateTime.getTime();

            if (timeDifference >= timeUpdate) {
                double totalAmount = order.getTotalAmount();
                double percent = 10 - order.getSeller().vip;
                double orderCoints = totalAmount - (totalAmount * percent / 100);//tiền nhận
                order.setStatus("SUCCESS");
                order.getSeller().setCoint(order.getTotalAmount() + orderCoints);
                userService.save(order.getSeller());
                orderService.saveOrder(order);
                Notification notification = new Notification();
                notification.setUser(order.getSeller());

                notification.setContent("Thông Báo hoàn thành đơn hàng");
                notification.setTitle("Đơn hàng số " + order.getOrderId() + " có mã tracking là " + order.getCode() + " đã được người dùng xác nhận thành công");
                notification.setNote("Đơn hàng của bạn đã được xác nhận thành công," +
                        " bạn nhận được " + orderCoints + " Coints (đã trừ chiết khấu " + percent + "%)");
                notification.setReadStatus(false);
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notificationRepository.save(notification);
            }

            double cointFromDB = userRepository.findCointById(order.getSeller().userId);
            order.getSeller().setCoint(cointFromDB);
            userRepository.save(order.getSeller());
        }
        userRepository.save(loggedInUser);
    }
//------------------------------------------------------------------------------
    }

}