package com.example.productmanager.service;

import com.example.productmanager.dao.ReviewRepository;
import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.Review;
import com.example.productmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void saveReview(Review review) {
        // Thực hiện lưu đánh giá vào cơ sở dữ liệu
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProduct_ProductId(productId);
    }


    public boolean hasUserReviewedProductInOrder(User user, Product product, Order order) {
        // Kiểm tra xem có đánh giá nào của người dùng cho sản phẩm trong đơn hàng không
        List<Review> reviews = reviewRepository.findByUserAndProductAndOrder(user, product, order);

        // Nếu có bất kỳ bản ghi nào, người dùng đã đánh giá sản phẩm trong đơn hàng
        return !reviews.isEmpty();
    }

    public List<Review> getReviewsByUser(long loggedInUser) {
        return reviewRepository.findByUser_UserId(loggedInUser);
    }
}
