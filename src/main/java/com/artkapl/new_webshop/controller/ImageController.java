package com.artkapl.new_webshop.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.artkapl.new_webshop.dto.ImageDto;
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
@RequestMapping("${api.url.prefix}/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Value("${upload.directory}")
    private String uploadDir;

    @PostMapping("/product/{productId}")  
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @PathVariable Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId, uploadDir);
            return ResponseEntity.ok(new ApiResponse("Image(s) saved!", imageDtos));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws IOException {
        try {
            Image image = imageService.getImageById(imageId);

            Path filePath = Paths.get(uploadDir).resolve(String.valueOf(image.getProduct().getId())).resolve(image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1));
    
            // Check if the file exists
            if (!filePath.toFile().exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
    
            // Load the file as a resource
            Resource resource = new UrlResource(filePath.toUri());
    
            if (resource.exists() || resource.isReadable()) {
                // Return the image inline
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(image.getFileType()))
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/product/{productId}/image/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long productId, @PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            List<ImageDto> imageDtos = imageService.updateImage(file, productId, imageId, uploadDir);
            return ResponseEntity.ok(new ApiResponse("Image updated!", imageDtos));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok(new ApiResponse("Image deleted!", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ControllerTools.getInternalErrorResponse(e);
        }
    }

}
