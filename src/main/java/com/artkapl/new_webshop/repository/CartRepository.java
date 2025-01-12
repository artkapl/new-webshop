package com.artkapl.new_webshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artkapl.new_webshop.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
