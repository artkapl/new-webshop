package com.artkapl.new_webshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artkapl.new_webshop.exception.AlreadyExistsException;
import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Category;
import com.artkapl.new_webshop.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        Category existingCategory = categoryRepository.findByName(category.getName());
        // Category name must be unique
        if (existingCategory != null) {
            throw new AlreadyExistsException("A Category with the name " + category.getName() + " already exists!");
        }
        Category newCategory = new Category(
            category.getName(),
            category.getDescription()
        );
        return categoryRepository.save(newCategory);
            

    }

    @Override
    public Category updateCategory(Category category, Long id) {
        // Get existing category
        Category existingCategory = getCategoryById(id);
        // Update category values
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found!"));
        categoryRepository.delete(category);
    }

}
