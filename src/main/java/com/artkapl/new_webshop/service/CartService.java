package com.artkapl.new_webshop.service;

import java.math.BigDecimal;

import com.artkapl.new_webshop.model.Cart;

public interface CartService {
    Cart getCart(Long id);
    void emptyCart(Long id);
    BigDecimal getTotalPrice(Long id);

}
