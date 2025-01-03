package com.artkapl.new_webshop.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.artkapl.new_webshop.model.Product;
import com.artkapl.new_webshop.request.ProductCreateRequest;
import com.artkapl.new_webshop.request.ProductUpdateRequest;

public interface ProductService {
    Product createProduct(ProductCreateRequest request);
    Product getProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    Page<Product> getPaginatedProducts(int page, int size);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByManufacturer(String manufacturer);
    List<Product> getProductsByName(String productName);
    Long countByManufacturerAndName(String manufacturer, String name);

}
