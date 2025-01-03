package com.artkapl.new_webshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Category;
import com.artkapl.new_webshop.model.Product;
import com.artkapl.new_webshop.repository.CategoryRepository;
import com.artkapl.new_webshop.repository.ProductRepository;
import com.artkapl.new_webshop.request.ProductCreateRequest;
import com.artkapl.new_webshop.request.ProductUpdateRequest;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(ProductCreateRequest request) {
        // Check if category is already in DB
        // Case: Yes --> set as product category
        // Case: No --> Save as new Category in DB (and set as product category)
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
            .orElseGet(() -> {
                Category newCategory = new Category(
                    request.getCategory().getName(), 
                    request.getCategory().getDescription());
                return categoryRepository.save(newCategory);
            });
        request.setCategory(category);
        
        // Create Product with category in DB
        return productRepository.save(createProductWithCategory(request, category));
}

    private Product createProductWithCategory(ProductCreateRequest request, Category category) {
        return new Product(
            request.getName(),
            request.getManufacturer(),
            request.getDescription(),
            request.getPrice(),
            request.getInventory(),
            category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found!"));
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        // Get existing product by productId
        // Update existing product with values from request
        // Save updated product in DB
        // Throw exception if product with ID not found in DB.
        return productRepository.findById(productId)
            .map(existingProduct -> updateExistingProduct(existingProduct, request))
            .map(productRepository::save)
            .orElseThrow(() -> new NotFoundException("Product not found!"));

    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        // Update product fields
        existingProduct.setName(request.getName());
        existingProduct.setManufacturer(request.getManufacturer());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());

        // Get Category from DB and update product category
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
            .ifPresentOrElse(productRepository::delete, 
                () -> {
                    throw new NotFoundException("Product not found!");
                });
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getPaginatedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
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
