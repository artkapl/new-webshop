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
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {throw new NotFoundException("Image not found!");});
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> imageFiles, Long productId, String uploadDir) throws IOException, SerialException, SQLException {
        Path productDir = getProductDir(productId, uploadDir);
        // Ensure the product-specific directory exists
        if (!Files.exists(productDir)) {
            Files.createDirectories(productDir);
        }

        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            checkIsImageFile(file);

            String newFilename = saveImageAndGetFilePath(productId, productDir, file);

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

    @SuppressWarnings("null")
    private void checkIsImageFile(MultipartFile file) {
        // Ensure the file is an image (optional, but good practice)
        if (file == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }
    }

    private Path getProductDir(Long productId, String uploadDir) {
        Path rootDir = Paths.get("").toAbsolutePath();  // Project root directory
        // Resolve the directory path where files will be saved
        Path productDir = rootDir.resolve(uploadDir).resolve(String.valueOf(productId));
        return productDir;
    }

    private String saveImageAndGetFilePath(Long productId, Path productDir, MultipartFile file) throws IOException {
        // Resolve the file path where the image will be stored
        String newFilename = System.currentTimeMillis() + "_" + productId + "_" + file.getOriginalFilename();
        Path filePath = productDir.resolve(newFilename);

        // Save the file to the specified directory
        file.transferTo(filePath.toFile());
        return newFilename;
    }

    private ImageDto createImageDto(Image savedImage) {
        ImageDto imageDto = new ImageDto();
        imageDto.setFileName(savedImage.getFileName());
        imageDto.setId(savedImage.getId());
        imageDto.setImageUrl(savedImage.getImageUrl());
        return imageDto;
    }

    private Image createImage(MultipartFile file) throws SerialException, SQLException, IOException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        return image;
    }

    @Override
    public List<ImageDto> updateImage(MultipartFile imageFile, Long productId, Long imageId, String uploadDir) {
        Image image = getImageById(imageId);
        List<ImageDto> newImageDtos = new ArrayList<>();
        try {
            checkIsImageFile(imageFile);  // verify new imageFile

            // delete old image
            String filePath = Paths.get(uploadDir)
                .resolve(String.valueOf(image.getProduct().getId()))
                .resolve(image.getImageUrl()
                .substring(image.getImageUrl().lastIndexOf("/") + 1)).toString();
            deleteImageFromDisk(filePath);

            // save new image on disk and in DB
            List<MultipartFile> images = new ArrayList<>();
            images.add(imageFile);
            newImageDtos = saveImages(images, productId, uploadDir);

            // mark image as orphaned for cleanup scheduler
            image.setOrphaned(true);
            imageRepository.save(image);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return newImageDtos;
    }

    public void deleteImageFromDisk(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

}
