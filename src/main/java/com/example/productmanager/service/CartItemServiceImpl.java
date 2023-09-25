package com.example.productmanager.service;

import com.example.productmanager.dao.CartItemRepository;
import com.example.productmanager.entity.CartItem;
import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

//    @Override
//    public List<CartItem> getAllCartItems() {
//        return cartItemRepository.findAll();
//    }
//    @Override
//    public List<CartItem> getCartItemByUserId(Long userId) {
//        return cartItemRepository.findAllByUserUserId(userId);
//    }

    @Override
    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cart item id"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        int maxQuantity = cartItem.getProduct().getQuantityStock();
        if (quantity > maxQuantity) {
            quantity = cartItem.getProduct().quantityStock;
        }
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

//    @Override
//    public List<CartItem> getCartItemByUserIdAndSellerId(Long userId, Long sellerId) {
//        return cartItemRepository.findByUserUserIdAndProductSellerUserId(userId, sellerId);
//    }

    @Override
    public List<CartItem> getCartItemByUserIdOrderByCreatedAtDesc(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserUserIdOrderByCreatedAtDesc(userId);

        // Loại bỏ các sản phẩm có product = null hoặc quantityStock = 0 hoặc quantity = 0 hoặc sp chính mình
        cartItems.removeIf(cartItem -> cartItem.getProduct() == null
                || cartItem.getProduct().getQuantityStock() == 0
                || cartItem.getQuantity() == 0
        || cartItem.getProduct().getSeller().getUserId().equals(userId));

        return cartItems;
    }


    @Override
    public void addToCart(User loggedInUser, Long productId) {
        Product product = productService.findById(productId.intValue());
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        // Kiểm tra số lượng tồn kho của sản phẩm
        if (product.quantityStock <= 0) {
            System.out.println("Out of stock");
        }

        // Kiểm tra xem sản phẩm có thuộc về chính người dùng đang đăng nhập không
        if (product.getSeller().getUserId().equals(loggedInUser.getUserId())) {
            System.out.println("You cannot add your own product to the cart");
        }

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(loggedInUser, product);
        if (existingCartItem != null) {
            // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
            existingCartItem.setQuantity(existingCartItem.quantity + 1);
            cartItemRepository.save(existingCartItem);
        } else {
            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, tạo mới CartItem
            CartItem cartItem = new CartItem();
            cartItem.setUser(loggedInUser);
            cartItem.setProduct(product);
            cartItem.setQuantity(1); // Số lượng mặc định là 1
            cartItem.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public List<CartItem> getCartItemsByIds(List<Long> cartItemIds) {
        return cartItemRepository.findAllById(cartItemIds);
    }

    @Override
    public void deleteCartItems(List<Long> cartItemIds) {
        cartItemRepository.deleteAllByIdIn(cartItemIds);
    }

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }
    @Override
    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }



}
