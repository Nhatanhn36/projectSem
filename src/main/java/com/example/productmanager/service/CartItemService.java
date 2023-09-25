package com.example.productmanager.service;

import com.example.productmanager.entity.CartItem;
import com.example.productmanager.entity.User;

import java.util.List;

public interface CartItemService {
    void deleteCartItems(List<Long> cartItemIds);
    CartItem getCartItemById(Long id);

    void saveCartItem(CartItem cartItem);

    void deleteCartItem(Long id);



    void updateCartItemQuantity(Long cartItemId, int quantity);


    List<CartItem> getCartItemByUserIdOrderByCreatedAtDesc(Long userId);

    void addToCart(User loggedInUser, Long productId);

    List<CartItem> getCartItemsByIds(List<Long> selectedItems);
}
