    package com.example.productmanager.service;

    import com.example.productmanager.controller.UserController;
    import com.example.productmanager.dao.NotificationRepository;
    import com.example.productmanager.dao.RegisterSellerRepository;
    import com.example.productmanager.dao.UserRepository;
    import com.example.productmanager.entity.*;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.sql.Timestamp;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final RegisterSellerRepository registerSellerRepository; // Declare the repository
        private final CartItemService cartItemService;
        private final NotificationRepository notificationRepository;

        @Autowired
        public UserService(UserRepository userRepository, RegisterSellerRepository registerSellerRepository, CartItemService cartItemService, NotificationRepository notificationRepository) {
            this.registerSellerRepository = registerSellerRepository; // Inject the repository
            this.userRepository = userRepository;
            this.cartItemService = cartItemService;
            this.notificationRepository = notificationRepository;
        }
        public List<User> findAll() {
            return userRepository.findAll();
        }

        public User findByUsername(String username) {
            return userRepository.findByUsername(username);
        }
        public User findById(Long userId) {
            Optional<User> result = userRepository.findById(userId);
            return result.orElse(null);
        }

        public void save(User user) {
            userRepository.save(user);
        }

        public void deleteById(Long userId) {
            userRepository.deleteById(userId);
        }

        public User authenticateUser(String username, String rawPassword) {
            User user = userRepository.findByUsername(username);

            if (user != null) {
                if (checkPassword(rawPassword, user.getPassword())) {
                    return user;
                }
            }
            return null;
        }
        public boolean checkPassword(String rawPassword, String encodedPassword) {
            return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
        }
        public List<User> findSellers() {
            return userRepository.findByRole((byte) UserController.UserRole.SELLER.getValue());
        }

        public User getUserById(Long sellerId) {
            return userRepository.findById(sellerId).orElse(null);
        }


        // Phê duyệt yêu cầu tài khoản Seller
        public void approveSellerRequest(Long requestId) {
            Optional<RegisterSeller> optionalRegisterSeller = registerSellerRepository.findById(requestId);
            optionalRegisterSeller.ifPresent(userI -> {
                        userI.getUser().role = 1;

                        // Lấy thời gian thực
                        LocalDateTime currentTime = LocalDateTime.now();

                        // Định dạng thời gian theo yêu cầu
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedTime = currentTime.format(formatter);

                        userI.setUpdate_time((formattedTime));
                        userI.setStatus("ACCEPT"); // Đặt status khi phê duyệt
                        registerSellerRepository.save(userI);


                Timestamp concac = new Timestamp(System.currentTimeMillis());
                Notification notification = new Notification();
                notification.setUser(userI.getUser());
                notification.setCreatedAt(concac);
                notification.setTitle("Yêu cầu người bán đã được phê duyệt");
                notification.setContent("Chúc mừng bạn đã trở thành Seller của ShopPlaza");
                notification.setNote("Chúng tôi đã phê duyệt yêu cầu đăng kí của bạn vào lúc "
                        + concac + ",từ giờ hãy trở thành 1 nhà bán hàng thật tốt nhé!");
                notification.setReadStatus(false);
                notificationRepository.save(notification);

                    });
        }
        // từ chối seller
        public void declineSellerRequest(Long requestId) {
            Optional<RegisterSeller> optionalRegisterSeller
                    = registerSellerRepository.findById(requestId);
            optionalRegisterSeller.ifPresent(userI -> {
                        userI.setStatus("DECLINE"); // Đặt status khi từ duyệt
                        registerSellerRepository.save(userI);


                Timestamp concac = new Timestamp(System.currentTimeMillis());
                Notification notification = new Notification();
                notification.setUser(userI.getUser());
                notification.setCreatedAt(concac);
                notification.setTitle("Yêu cầu phê duyệt bị từ chối!");
                notification.setContent("Rất tiếc,bạn chưa đạt điều kiện để trở thành người bán tại Shop Plaza");
                notification.setNote("Sau khi xem xét,chúng tôi nhận thấy bạn chưa đạt" +
                        " yêu cầu để trở thành người bán,vui lòng cập nhật lại thông tin và yêu cầu xem xét lại nếu bạn muốn");
                notification.setReadStatus(false);
                notificationRepository.save(notification);

                    }
            );
            }
        // Đăng ký yêu cầu tài khoản Seller
        public void registerSellerRequest(Long userId, String description) {
            RegisterSeller registerSeller = new RegisterSeller();
            registerSeller.setUser(userRepository.getById(userId));
            // Lấy thời gian thực
            LocalDateTime currentTime = LocalDateTime.now();
            // Định dạng thời gian theo yêu cầu
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedTime = currentTime.format(formatter);
            registerSeller.setUpdate_time((formattedTime));
            registerSeller.setRegistrationDate((formattedTime));
            registerSeller.setDescription(description);
            registerSellerRepository.save(registerSeller);
        }

        // Lấy danh sách yêu cầu đăng ký tài khoản Seller
        public List<RegisterSeller> getAllSellerRequests() {
            return registerSellerRepository.findAll();
        }

        public RegisterSeller getSellerRequestById(Long id) {
            return registerSellerRepository.getSellerRequestById(id);
        }
        public List<RegisterSeller> getSellerRequestsByUserId(Long userId) {
            return registerSellerRepository.findAllByUserUserId(userId);
        }


        public List<RegisterSeller> getSellerRequestsByStatusAndUserId(String status, Long userId) {
            return registerSellerRepository.findAllByStatusAndUserUserId(status, userId);
        }

        public int countSellerRequestsByStatusAndUserId(String status, Long userId) {
            return registerSellerRepository.countAllByStatusAndUserUserId(status, userId);
        }
        public int countSellerAllRequestsByStatus(String status) {
            return registerSellerRepository.countAllByStatus(status);
        }


        public List<User> getSellersByLoggedInUser(User loggedInUser) {
            List<User> sellers = new ArrayList<>();

            List<CartItem> cartItems = cartItemService.getCartItemByUserIdOrderByCreatedAtDesc(loggedInUser.getUserId());

            for (CartItem cartItem : cartItems) {
                User seller = cartItem.getProduct().getSeller();
                if (!sellers.contains(seller)) {
                    sellers.add(seller);
                }
            }

            return sellers;
        }





    }
