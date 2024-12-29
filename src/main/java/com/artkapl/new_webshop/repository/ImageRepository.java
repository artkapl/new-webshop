package com.artkapl.new_webshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artkapl.new_webshop.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
