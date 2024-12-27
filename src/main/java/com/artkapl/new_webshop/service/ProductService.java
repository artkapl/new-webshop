package com.artkapl.new_webshop.service;

import java.util.List;

import com.artkapl.new_webshop.model.Product;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    void updateProduct(Product product, Long productId);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByManufacturer(String manufacturer);
    List<Product> getProductsByName(String productName);
    Long countByManufacturerAndName(String manufacturer, String name);

}
