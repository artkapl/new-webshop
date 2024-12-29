package com.artkapl.new_webshop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.artkapl.new_webshop.dto.ImageDto;
import com.artkapl.new_webshop.model.Image;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImage(Long id);
    List<ImageDto> saveImages(List<MultipartFile> imageFiles, Long productId);
    Image updateImage(MultipartFile imageFile, Long imageId);
}
