package com.artkapl.new_webshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.artkapl.new_webshop.exceptions.ProductNotFoundException;
import com.artkapl.new_webshop.model.Product;
import com.artkapl.new_webshop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProduct'");
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void updateProduct(Product product, Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
            .ifPresentOrElse(productRepository::delete, 
                () -> {throw new ProductNotFoundException("Product not found");});
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public Long countByManufacturerAndName(String manufacturer, String name) {
        return productRepository.countByManufacturerAndName(manufacturer, name);
    }

    

}
