package com.example.productmanager.controller;

import com.example.productmanager.dao.NotificationRepository;
import com.example.productmanager.entity.*;
import com.example.productmanager.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final OrderDetailsService orderDetailsService;
    private final OrderService orderService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public CartItemController(CartItemService cartItemService,
                              UserService userService,
                              ProductService productService,
                              OrderItemService orderItemService,
                              OrderDetailsService orderDetailsService,
                              OrderService orderService, NotificationRepository notificationRepository) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.orderDetailsService = orderDetailsService;
        this.orderService = orderService;
        this.notificationRepository = notificationRepository;
    }


    @PostMapping("/update-quantity")
    public String updateCartItemQuantity(@RequestParam("cartItemId") Long cartItemId,
                                         @RequestParam("quantity") int quantity) {
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            // Handle cart item not found
            return "redirect:/cart/list";
        }

        // Ensure quantity is within the valid range (1 to quantity_stock)
        int newQuantity = Math.max(1, Math.min(quantity, cartItem.getProduct().getQuantityStock()));
        cartItem.setQuantity(newQuantity);
        cartItemService.saveCartItem(cartItem);

        return "redirect:/cart/list";
    }

    @PostMapping("/updateQuantity/{id}")
    public String updateCartItemQuantity(@PathVariable("id") Long cartItemId,
                                         @RequestParam("quantity") int quantity,HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        // Cập nhật số lượng sản phẩm trong giỏ hàng
        cartItemService.updateCartItemQuantity(cartItemId, quantity);

        return "redirect:/cart/list";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        Product product = productService.findById(productId.intValue());

        if (product.getSeller().getUserId().equals(loggedInUser.getUserId())) {
            model.addAttribute("error", "You cannot add your own product to the cart");
        }

        cartItemService.addToCart(loggedInUser, productId);

        return "redirect:/cart/list";
    }




    @PostMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable("id") Long cartItemId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        // Xóa sản phẩm khỏi giỏ hàng
        cartItemService.deleteCartItem(cartItemId);

        return "redirect:/cart/list";
    }








    String randomMaTracking() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 5;
        Random random = new Random();

        String code;
        do {
            StringBuilder codeBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                char randomChar = characters.charAt(index);
                codeBuilder.append(randomChar);
            }
            code = codeBuilder.toString();
        } while (trackingCodeExists(code)); // Kiểm tra nếu mã đã tồn tại

        return code;
    }

    boolean trackingCodeExists(String code) {
        return orderService.orderExistsWithCode(code);
    }


    @GetMapping("/list")
    public String listCartItems(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("loggedInUser", loggedInUser);

        List<CartItem> cartItems = cartItemService.getCartItemByUserIdOrderByCreatedAtDesc(loggedInUser.getUserId());

        List<User> sellers = userService.getSellersByLoggedInUser(loggedInUser);

        // Tính tổng giá trị của giỏ hàng
        double cartItemsTotalAmount = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();

        model.addAttribute("sellers", sellers);
        model.addAttribute("cartItemsTotalAmount", cartItemsTotalAmount);
        model.addAttribute("cartItems", cartItems);

        return "cart/list-cart-items";
    }



    // Hàm tính tổng số coints cần để thanh toán dựa trên danh sách sản phẩm đã chọn
    private int calculateRequiredCoints(List<Long> selectedItems) {
        int totalRequiredCoints = 0;

        // Lặp qua danh sách sản phẩm đã chọn để tính tổng số coints cần
        List<CartItem> cartItems = cartItemService.getCartItemsByIds(selectedItems);
        for (CartItem cartItem : cartItems) {
            int cointsPerItem = (int) (cartItem.getProduct().price); // Số coints cần cho mỗi sản phẩm
            int quantity = cartItem.getQuantity(); // Số lượng sản phẩm
            int requiredCointsForItem = cointsPerItem * quantity; // Tổng coints cần cho sản phẩm này
            totalRequiredCoints += requiredCointsForItem; // Cộng dồn vào tổng coints cần
        }

        return totalRequiredCoints;
    }



        @Transactional
    @PostMapping("/place-order")
    public String placeOrder(@RequestParam("selectedItems") List<Long> selectedItems,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam("fullname") String fullname,
                             @RequestParam("phone") String phone,
                             @RequestParam("address") String address
                ,Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
            int userCoints = (int) loggedInUser.coint;
            int requiredCoints = calculateRequiredCoints(selectedItems);

            if ("COINTS".equals(paymentMethod) && userCoints < requiredCoints) {
                return "/cart/not-enough-coints";
            }



            Map<User, List<CartItem>> itemsBySeller = new HashMap<>();
            List<String> orderNotifications = new ArrayList<>();

        // Phân chia sản phẩm theo người bán
        List<CartItem> cartItems = cartItemService.getCartItemsByIds(selectedItems);
        for (CartItem cartItem : cartItems) {
            User seller = cartItem.getProduct().getSeller();
            itemsBySeller.computeIfAbsent(seller, k -> new ArrayList<>()).add(cartItem);
        }

            for (Map.Entry<User, List<CartItem>> entry : itemsBySeller.entrySet()) {
                User seller = entry.getKey();
                List<CartItem> sellerCartItems = entry.getValue();
                double totalAmount = sellerCartItems.stream()
                        .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                        .sum();

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : sellerCartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantityOrder(cartItem.getQuantity());
                orderItem.setTotalMonneyProduct(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                cartItem.getProduct().setQuantityStock(cartItem.getProduct().getQuantityStock() - cartItem.getQuantity());
                productService.save(cartItem.getProduct());
                orderItems.add(orderItem);
            }
                Order order = new Order();
                order.setUser(loggedInUser);
                order.setSeller(seller);
                order.setTotalAmount(totalAmount);
                order.setCode("SHOPPLAZA" + randomMaTracking());
                order.setStatus("PENDING");
                orderService.saveOrder(order);

                double percent = 10 - order.getSeller().vip;
                double totalReceived = totalAmount - (totalAmount * percent / 100);//tiền nhận
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(order);
                orderDetails.setSoTienGoc(totalAmount);
                orderDetails.setSoTienNhan(totalReceived);
                orderDetails.setPercent(percent);
                if ("COINTS".equals(paymentMethod)){
                    loggedInUser.coint -= totalAmount;
                    System.out.println("OK");
                    userService.save(loggedInUser);
                }
                orderDetails.setPayment(paymentMethod);
                orderDetails.setTenNguoiNhan(fullname);
                orderDetails.setDiachiNguoiNhan(address);
                orderDetails.setPhoneNguoiNhan(phone);
                orderDetailsService.saveOrderDetails(orderDetails);
                orderDetails.setOrderId(order);
                orderDetailsService.saveOrderDetails(orderDetails);
                cartItemService.deleteCartItems(selectedItems);
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrderId(order);
                orderItemService.saveOrderItem(orderItem);
            }
//--------------------- thông báo ng dùng
                orderNotifications.add("Mã đơn hàng: " + order.getCode());
                String notificationContent = "Bạn đã đặt các đơn hàng sau:\n" +
                        String.join("\n", orderNotifications);
                Notification notification = new Notification();
                notification.setUser(loggedInUser);
                notification.setContent(notificationContent);
                notification.setTitle("Thông báo đặt hàng Thành Công");
                notification.setNote("Vui lòng nhận hàng bạn đã đặt"); // Bạn có thể thêm ghi chú nếu cần
                notification.setReadStatus(false);
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notificationRepository.save(notification);
//-----------------------------------------------
                Notification notify = new Notification();
                notify.setUser(order.getSeller());
                notify.setContent("Vui lòng chuẩn bị đơn hàng!");
                notify.setTitle("Thông Báo Có Đơn Hàng Mới");
                notify.setNote("Người Mua " + loggedInUser.username + " đã đặt đơn hàng "+ order.getCode() +" từ bạn,vui lòng xử lí đơn hàng này!"); // Bạn có thể thêm ghi chú nếu cần
                notify.setReadStatus(false);
                notify.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notificationRepository.save(notify);

            }

        return "redirect:/orders/list";
    }


    @GetMapping("/checkout")
    public String checkoutForm(@RequestParam("selectedItems") List<Long> selectedItems, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
            List<CartItem> cartItems = cartItemService.getCartItemsByIds(selectedItems);
            double cartItemsTotalAmount = cartItems.stream()
                    .mapToDouble(cartItem ->
                            cartItem.getProduct().getPrice() * cartItem.getQuantity())
                    .sum();

        // Tính tổng số coints cần để thanh toán
        int requiredCoints = calculateRequiredCoints(selectedItems);
        int coints = (int) loggedInUser.coint;

        model.addAttribute("requiredCoints", requiredCoints);
        model.addAttribute("coints", coints);

        model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("cartItemsTotalAmount", cartItemsTotalAmount);
            model.addAttribute("cartItems", cartItems);
            return "cart/checkout-form";
    }

    @GetMapping("/order-confirmation/{orderId}")
    public String orderConfirmation(@PathVariable("orderId") Long orderId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getUser().getUserId().equals(loggedInUser.getUserId())) {
            return "redirect:/cart/list";
        }

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("order", order);

        return "cart/order-confirmation";
    }
}
