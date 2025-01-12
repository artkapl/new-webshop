package com.artkapl.new_webshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artkapl.new_webshop.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
