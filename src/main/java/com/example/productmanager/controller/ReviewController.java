package com.example.productmanager.controller;

import com.example.productmanager.entity.*;
import com.example.productmanager.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final OrderServiceImpl orderService;
    private final OrderItemService orderItemService;
    private final ImageService imageService;
    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, OrderServiceImpl orderService, OrderItemService orderItemService, ImageService imageService, ProductService productService) {
        this.reviewService = reviewService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.imageService = imageService;
        this.productService = productService;
    }




    @GetMapping("/myreviews")
    public String viewMyReviews(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        // Truy vấn cơ sở dữ liệu để lấy danh sách các đánh giá của người dùng
        List<Review> userReviews = reviewService.getReviewsByUser(loggedInUser.userId);

        // Truyền danh sách đánh giá vào model để hiển thị trong mẫu HTML
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("loggedInUser", loggedInUser);

        return "review/my_reviews";
    }




    @GetMapping("/create/{orderId}")
    public String createReviewForm(@PathVariable Order orderId,
                                   HttpSession session,
                                   Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model



        // Lấy thông tin đơn hàng bằng orderId (sử dụng service của bạn)
        Order order = orderService.findById(orderId);

        // Kiểm tra xem đơn hàng tồn tại và đã hoàn thành (status là "SUCCESS") chưa
        if (order == null || !order.getStatus().equals("SUCCESS")) {
            // Xử lý khi đơn hàng không tồn tại hoặc chưa hoàn thành
            return "redirect:/orders/list"; // Hoặc chuyển hướng đến trang khác tùy ý
        }





        // Lấy danh sách các sản phẩm trong đơn hàng
        List<OrderItem> ordersItem = orderItemService.findByOrderId(order);

        // Kiểm tra xem người dùng đã đánh giá sản phẩm trong đơn hàng này chưa
        boolean userHasReviewed = false;
        for (OrderItem orderItems : ordersItem) {
            Product product = orderItems.getProduct();
            if (reviewService.hasUserReviewedProductInOrder(loggedInUser, product, order)) {
                userHasReviewed = true;
                break; // Nếu đã đánh giá một sản phẩm, dừng kiểm tra
            }
        }

        // Nếu người dùng đã đánh giá sản phẩm trong đơn hàng này, chuyển hướng họ đến trang danh sách đánh giá
        if (userHasReviewed) {
            return "redirect:/reviews/myreviews"; // Hoặc chuyển hướng đến trang khác tùy ý
        }





        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        // Tạo danh sách các đánh giá (mỗi sản phẩm một đánh giá)
        List<Review> reviews = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {

            Review review = new Review();
            review.setOrder(order);
            review.setProduct(orderItem.getProduct());
            reviews.add(review);
        }

        // Truyền danh sách đánh giá và danh sách sản phẩm vào model
        model.addAttribute("reviews", reviews);
        model.addAttribute("order", order);

        return "review/review_form"; // Thay thế bằng tên template thích hợp
    }

    @PostMapping("/create/{orderId}")
    public String createReview(@PathVariable Long orderId,
                               @ModelAttribute("review") Review review,
                               @RequestParam("imageReview") MultipartFile imageReview
    ,HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model


        // Lấy thông tin đơn hàng bằng orderId (sử dụng service của bạn)
        Order order = orderService.findById(orderId);

        // Kiểm tra xem đơn hàng tồn tại và đã hoàn thành (status là "SUCCESS") chưa
        if (order == null || !order.getStatus().equals("SUCCESS")) {
            // Xử lý khi đơn hàng không tồn tại hoặc chưa hoàn thành
            return "redirect:/orders/list"; // Hoặc chuyển hướng đến trang khác tùy ý
        }

        // Lấy danh sách các sản phẩm trong đơn hàng
        List<OrderItem> orderItems = orderItemService.findByOrderId(order);

        // Lặp qua từng sản phẩm trong đơn hàng và lưu đánh giá cho từng sản phẩm
        for (OrderItem orderItem : orderItems) {
            Review productReview = new Review();
            productReview.setUser(loggedInUser);
            productReview.setOrder(order);
            productReview.setProduct(orderItem.getProduct());
            if (imageReview != null && !imageReview.isEmpty()) {
                try {
                    String newImageFileName = imageService.uploadImage(imageReview);
                    productReview.setImagesReview(newImageFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            productReview.setRating(review.getRating());
            productReview.setComment(review.getComment());
            reviewService.saveReview(productReview);
        }

        return "redirect:/reviews/" + orderItems.get(0).getProduct().getProductId() + "/list";
    }


    @GetMapping("/{productId}/list")
    public String listReviews(@PathVariable Long productId, Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        Product product = productService.findById(Math.toIntExact(productId));
        model.addAttribute("productReview", product);
        model.addAttribute("reviews", reviews);

        return "review/review_list";
    }
}
