        package com.example.productmanager.controller;

        import com.example.productmanager.dao.NotificationRepository;
        import com.example.productmanager.dao.RegisterSellerRepository;
        import com.example.productmanager.entity.Notification;
        import com.example.productmanager.entity.RegisterSeller;
        import com.example.productmanager.entity.User;
        import com.example.productmanager.entity.UserInfo;
        import com.example.productmanager.service.UserInfoService;
        import com.example.productmanager.service.UserService;
        import jakarta.servlet.http.HttpSession;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;

        import java.sql.Timestamp;
        import java.util.Comparator;
        import java.util.List;
        import java.util.stream.Collectors;

        @Controller
        @RequestMapping("/seller")
        public class SellerController {





            private final NotificationRepository notificationRepository;
            private final UserService userService;
            private final UserInfoService userInfoService;
            private final RegisterSellerRepository registerSellerRepository;

            @Autowired
            public SellerController(NotificationRepository notificationRepository, UserService sellerService, UserInfoService userInfoService, RegisterSellerRepository registerSellerRepository) {
                this.notificationRepository = notificationRepository;
                this.userService = sellerService;
                this.userInfoService = userInfoService;
                this.registerSellerRepository = registerSellerRepository;
            }
            //====================================================================================

            @GetMapping("/register")
            public String showRegisterForm(Model model, HttpSession session,User user) {
                // Lấy thông tin người dùng từ session
                User loggedInUser = (User) session.getAttribute("loggedInUser");
                if (loggedInUser == null) {
                    return "redirect:/users/login"; // Redirect to login if not logged in
                }
                if (loggedInUser.role == 1){
                    return "redirect:/seller/home"; // Redirect to login if not logged in
                }else  if (loggedInUser.role == 2){
                    return "redirect:/admin/home"; // Redirect to login if not logged in
                }
                UserInfo userInfo = userInfoService.getUserInfoByUserId(loggedInUser.getUserId());
                model.addAttribute("userInfo", userInfo);
                // Kiểm tra xem đã tồn tại thông tin người dùng cho userId này chưa
                if (userInfo == null) {
                    return "redirect:/user-info/create";
                }

                boolean isSellerRequestPending = registerSellerRepository.existsByUserUserIdAndStatus(loggedInUser.getUserId(), "Pending");

                if (isSellerRequestPending) {
                    return "redirect:/seller/list-register?status=PENDING";
                }

                model.addAttribute("loggedInUser", loggedInUser);
                return "seller/register"; // Assuming your registration form is named "register.html"
            }
            //====================================================================================

            @PostMapping("/register")
            public String registerSeller(@RequestParam("userId") Long userId,
                                         @RequestParam("description") String description,HttpSession session) {
                userService.registerSellerRequest(userId, description);
                User loggedInUser = (User) session.getAttribute("loggedInUser");



                Notification notification = new Notification();
                notification.setUser(loggedInUser);
                notification.setContent(" Admin đã nhận được câu trả lời của bạn");
                notification.setTitle("Gửi Đơn Đăng Kí Thành Công");
                notification.setNote("Quản trị viên đã nhận được thông tin về bạn,vui lòng chờ cho tới khi được phê duyệt\n" +
                        "Kết quả sẽ có sau 1-2 ngày làm việc");
                notification.setReadStatus(false);
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notificationRepository.save(notification);



                return "redirect:/seller/list-register?status=PENDING";
            }

            //====================================================================================
            @GetMapping("/list-register")
            public String getSellerRegistrationList(@RequestParam(name = "status", required = false) String status,
                                                    HttpSession session, Model model) {
                User loggedInUser = (User) session.getAttribute("loggedInUser");
                if (loggedInUser == null) {
                    return "redirect:/users/login";
                }
                model.addAttribute("loggedInUser", loggedInUser);

                // Kiểm tra xem người dùng có quyền xem danh sách đăng ký không
                if (loggedInUser.getRole() == 1) {
                    return "redirect:/seller/home"; // Chuyển hướng đến trang chủ hoặc nơi khác tùy bạn
                }
                List<RegisterSeller> requests;
                if (loggedInUser.role == 2){
                    requests = userService.getAllSellerRequests();

                    int pendingCount = userService.countSellerAllRequestsByStatus("PENDING" );
                    int acceptedCount = userService.countSellerAllRequestsByStatus("ACCEPT");
                    int declinedCount = userService.countSellerAllRequestsByStatus("DECLINE");

                    model.addAttribute("pendingCount", pendingCount);
                    model.addAttribute("acceptedCount", acceptedCount);
                    model.addAttribute("declinedCount", declinedCount);
                }else {
                    if ("PENDING".equals(status)) {
                        requests = userService.getSellerRequestsByStatusAndUserId("PENDING", loggedInUser.getUserId());
                    } else if ("ACCEPT".equals(status)) {
                        requests = userService.getSellerRequestsByStatusAndUserId("ACCEPT", loggedInUser.getUserId());
                    } else if ("DECLINE".equals(status)) {
                        requests = userService.getSellerRequestsByStatusAndUserId("DECLINE", loggedInUser.getUserId());
                    } else {
                        requests = userService.getSellerRequestsByUserId(loggedInUser.getUserId());
                    }

                    int pendingCount = userService.countSellerRequestsByStatusAndUserId("PENDING", loggedInUser.getUserId());
                    int acceptedCount = userService.countSellerRequestsByStatusAndUserId("ACCEPT", loggedInUser.getUserId());
                    int declinedCount = userService.countSellerRequestsByStatusAndUserId("DECLINE", loggedInUser.getUserId());
                    model.addAttribute("pendingCount", pendingCount);
                    model.addAttribute("acceptedCount", acceptedCount);
                    model.addAttribute("declinedCount", declinedCount);
                }
                model.addAttribute("requests", requests);
                model.addAttribute("loggedInUser", loggedInUser);


                if (status != null) {
                    // Lọc danh sách theo trạng thái
                    requests = requests.stream()
                            .filter(request -> request.getStatus().equals(status))
                            .collect(Collectors.toList());
                }

                // Sắp xếp theo update_time (mới nhất trước)
                requests = requests.stream()
                        .sorted(Comparator.comparing(RegisterSeller::getUpdate_time).reversed())
                        .collect(Collectors.toList());

                model.addAttribute("requests", requests);

                return "seller/register-list";
            }

            //====================================================================================

            @GetMapping("/details/{id}")

            public String getSellerRequestDetails(@PathVariable Long id, Model model,HttpSession session) {
                User loggedInUser = (User) session.getAttribute("loggedInUser");
                if (loggedInUser == null) {
                    return "redirect:/users/login"; // Redirect to login if not logged in
                }
                List<RegisterSeller> sellerRequests = userService.getAllSellerRequests();
                UserInfo userInfo = userInfoService.getUserInfoByUserId(loggedInUser.getUserId());
                model.addAttribute("userInfo", userInfo);
                model.addAttribute("requests", sellerRequests);
                model.addAttribute("loggedInUser", loggedInUser);
                RegisterSeller request = userService.getSellerRequestById(id);
                model.addAttribute("request", request);
                return "seller/register-details";
            }

            @PostMapping("/accept/{id}")
            public String approveSellerRequest(@PathVariable Long id,HttpSession session) {
                userService.approveSellerRequest(id);


                return "redirect:/seller/list-register";
            }

            @PostMapping("/decline/{id}")
            public String declineSellerRequest(@PathVariable Long id) {
                userService.declineSellerRequest(id);
                return "redirect:/seller/list-register";
            }


            @GetMapping("/home")
            public String homePage(HttpSession session, Model model) {
                User loggedInUser = (User) session.getAttribute("loggedInUser");
                model.addAttribute("loggedInUser", loggedInUser);
                if (loggedInUser == null) {
                    return "redirect:/users/login"; // Redirect to login if not logged in
                }
                return "seller/home";
            }
        }
