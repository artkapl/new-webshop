package com.artkapl.new_webshop.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Product;
import com.artkapl.new_webshop.request.ProductCreateRequest;
import com.artkapl.new_webshop.request.ProductUpdateRequest;
import com.artkapl.new_webshop.service.ProductService;
import com.artkapl.response.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("${api.url.prefix}/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", products));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPaginatedProducts(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "25") int size) {
        try {
            Page<Product> products = productService.getPaginatedProducts(page, size);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", products));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Success!", product));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategory(categoryName);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", products));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @GetMapping("/manufacturer/{manufacturerName}")
    public ResponseEntity<ApiResponse> getProductsByManufacturer(@PathVariable String manufacturerName) {
        try {
            List<Product> products = productService.getProductsByManufacturer(manufacturerName);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", products));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted!", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductCreateRequest productRequest) {
        try {
            Product newProduct = productService.createProduct(productRequest);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Product created!", newProduct));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProduct(productRequest, productId);
            return ResponseEntity.ok(new ApiResponse("Updated Product!", updatedProduct));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }
    
    
}
