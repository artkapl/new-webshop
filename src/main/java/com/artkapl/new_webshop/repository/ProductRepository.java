package com.artkapl.new_webshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artkapl.new_webshop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByManufacturerIgnoreCaseContaining(String manufacturer);

    List<Product> findByNameIgnoreCaseContaining(String productName);

    Long countByManufacturerAndName(String manufacturer, String name);

}
