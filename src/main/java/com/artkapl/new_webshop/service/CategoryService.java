package com.artkapl.new_webshop.service;

import java.util.List;

import com.artkapl.new_webshop.model.Category;

public interface CategoryService {
    Category getCategoryById(Long categoryId);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category createCategory(Category category);
    Category updateCategory(Category category, Long categoryId);
    void deleteCategory(Long id);

}
