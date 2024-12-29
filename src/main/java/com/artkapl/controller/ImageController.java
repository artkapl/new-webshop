package com.artkapl.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.artkapl.dto.ImageDto;
import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Image;
import com.artkapl.new_webshop.service.ImageService;
import com.artkapl.response.ApiResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("${api.prefix.url}/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/product/{productId}")  
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @PathVariable Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Image(s) saved!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Image upload failed! Please contact your administrator.", e.getMessage()));
        }

    }

    @GetMapping("/{imageId}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImageData().getBytes(0, (int) image.getImageData().length()));
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(image.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
            .body(resource);
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Image updated!", null));
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong: image update failed!", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImage(imageId);
                return ResponseEntity.ok(new ApiResponse("Image deleted!", null));
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong: image removal failed!", INTERNAL_SERVER_ERROR));
    }

}
