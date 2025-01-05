package com.artkapl.new_webshop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artkapl.new_webshop.dto.ImageDto;
import com.artkapl.new_webshop.exception.NotFoundException;
import com.artkapl.new_webshop.model.Image;
import com.artkapl.new_webshop.model.Product;
import com.artkapl.new_webshop.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductService productService;

    @Value("${server.url}")
    private String serverUrl;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new NotFoundException("Image not found!"));
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                () -> {
                    throw new NotFoundException("Image not found!");
                });
    }

    @SuppressWarnings("null")
    @Override
    public List<ImageDto> saveImages(List<MultipartFile> imageFiles, Long productId, String uploadDir) throws IOException, SerialException, SQLException {
        Path rootDir = Paths.get("").toAbsolutePath();  // Project root directory
        // Resolve the directory path where files will be saved
        Path productDir = rootDir.resolve(uploadDir).resolve(String.valueOf(productId));
        // Ensure the product-specific directory exists
        if (!Files.exists(productDir)) {
            Files.createDirectories(productDir);
        }

        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            // Ensure the file is an image (optional, but good practice)
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed.");
            }

            // Resolve the file path where the image will be stored
            String newFilename = System.currentTimeMillis() + "_" + productId + "_" + file.getOriginalFilename();
            Path filePath = productDir.resolve(newFilename);

            // Save the file to the specified directory
            file.transferTo(filePath.toFile());

            Image image = createImage(file);
            image.setProduct(product);

            // Create image download URL
            String imageUrl = serverUrl + "/uploads/" + productId + "/" + newFilename;
            image.setImageUrl(imageUrl);

            imageRepository.save(image);

            // Make ImageDto to return file name and url
            ImageDto imageDto = createImageDto(image);
            imageDtos.add(imageDto);
        }
        return imageDtos;
    }

    private ImageDto createImageDto(Image savedImage) {
        ImageDto imageDto = new ImageDto();
        imageDto.setFileName(savedImage.getFileName());
        imageDto.setImageId(savedImage.getId());
        imageDto.setImageUrl(savedImage.getImageUrl());
        return imageDto;
    }

    private Image createImage(MultipartFile file) throws SerialException, SQLException, IOException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        return image;
    }

    @Override  // TODO: fix update image
    public Image updateImage(MultipartFile imageFile, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(imageFile.getName());
            image.setFileType(imageFile.getContentType());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return image;
    }

}
