package com.example.productmanager.service;

import com.example.productmanager.dao.ProductRepository;
import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private UserService userService;
    private User user;

    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository) {
        productRepository = theProductRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    @Override
    public List<Product> findByHideOrderByCreatedAtDesc() {
        return productRepository.findByHideOrderByCreatedAtDesc(false);
    }
    @Override
    public List<Product> findZeroQuantityProductsBySeller(Long userId) {
        return productRepository.findBySellerUserIdAndQuantityStockEquals(userId, 0);
    }

    @Override
    public List<Product> findAvailableProductsByName(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndQuantityStockGreaterThanAndHideFalse(query, 0);
    }

    @Override
    public List<Product> findAvailableProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryIdAndQuantityStockGreaterThanAndHideFalse(categoryId, 0);
    }


    @Override
    public List<Product> findProductsBySeller(Long sellerId, Boolean hide) {
        return productRepository.findBySellerUserIdAndHideOrderByCreatedAtDesc(sellerId, hide);
    }

    @Override
    public List<Product> findAvailableProductsBySeller(Long sellerId) {
        return productRepository.findBySellerUserIdAndQuantityStockGreaterThanAndHideOrderByCreatedAtDesc(sellerId, 0, false);
        // Điều kiện "hide = false" có thể cần điều chỉnh theo yêu cầu thực tế.
    }

    @Override
    public List<Product> findHiddenProductsBySeller(Long sellerId) {
        return productRepository.findBySellerUserIdAndHideOrderByCreatedAtDesc(sellerId, true);
    }


    public List<Product> findByHideOrderByCreatedAtDesc(Boolean hide) {
        return productRepository.findByHideOrderByCreatedAtDesc(hide);
    }



    @Override
    public List<Product> findByQuantityStock(int quantity) {
        return productRepository.findByQuantityStock(quantity);
    }


    @Override
    public List<Product> findHiddenProducts() {
        return productRepository.findByHideOrderByCreatedAtDesc(true);
    }
    @Override
    public List<Product> findAvailableProducts() {
        return productRepository.findByQuantityStockGreaterThanAndHideOrderByCreatedAtDesc(0, false);
    }

    @Override
    public List<Product> findZeroQuantityProducts() {
        return productRepository.findByQuantityStock(0);
    }

    @Override
    public Product findById(int theId) {
        Optional<Product> result = productRepository.findById(theId);

        return result.orElseThrow(() -> new RuntimeException("Did not find product id - " + theId));
    }

    @Override
    public void save(Product theProduct) {
        productRepository.save(theProduct);
    }

    @Override
    public void deleteById(int theId) {
        productRepository.deleteById(theId);
    }
}
