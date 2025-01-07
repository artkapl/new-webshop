package com.artkapl.new_webshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artkapl.new_webshop.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductIsNull();
}
