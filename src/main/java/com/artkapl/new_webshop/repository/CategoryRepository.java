package com.artkapl.new_webshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artkapl.new_webshop.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String categoryName);

}
