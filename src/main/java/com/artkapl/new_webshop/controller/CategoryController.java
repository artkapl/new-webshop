package com.artkapl.new_webshop.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artkapl.new_webshop.model.Category;
import com.artkapl.new_webshop.service.CategoryService;
import com.artkapl.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(path = "${api.url.prefix}/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            String message = categories.isEmpty() ? "No categories found!" : "Categories found!";
            return ResponseEntity.ok(new ApiResponse(message, categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong!", INTERNAL_SERVER_ERROR));
        }
        
    }

}
