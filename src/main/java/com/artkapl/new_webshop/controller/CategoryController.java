package com.artkapl.new_webshop.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artkapl.new_webshop.exception.AlreadyExistsException;
import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Category;
import com.artkapl.new_webshop.service.CategoryService;
import com.artkapl.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




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
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Success", category));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryService.createCategory(category);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Category added!", newCategory));    
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

}
