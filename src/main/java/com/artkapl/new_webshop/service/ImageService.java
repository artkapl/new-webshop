package com.artkapl.new_webshop.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

import com.artkapl.new_webshop.dto.ImageDto;
import com.artkapl.new_webshop.model.Image;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImage(Long id);
    List<ImageDto> saveImages(List<MultipartFile> imageFiles, Long productId, String uploadDir) throws IOException, SerialException, SQLException;
    List<ImageDto> updateImage(MultipartFile imageFile, Long productId, Long imageId, String uploadDir);
    void deleteImageFromDisk(String filePath) throws IOException;
}
