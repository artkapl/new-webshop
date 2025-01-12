package com.artkapl.new_webshop.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Cart;
import com.artkapl.new_webshop.repository.CartItemRepository;
import com.artkapl.new_webshop.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Cart not found!"));

        BigDecimal totalAmount = cart.getTotalPrice();
        cart.setTotalPrice(totalAmount);

        return cartRepository.save(cart);
    }

    @Override
    public void emptyCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.delete(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalPrice();
    }

}
