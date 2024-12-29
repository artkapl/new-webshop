package com.artkapl.new_webshop.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artkapl.dto.ImageDto;
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

    private static final String IMAGE_URL_PATH = "/api/v1/images/download/";

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

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> imageFiles, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            try {
                // Save image
                Image image = createImage(file);
                image.setProduct(product);

                // Save image to Repository to get an ID for the download URL
                Image savedImage = imageRepository.save(image);

                // Create image download URL
                String downloadUrl = getDownloadUrl(savedImage);
                savedImage.setImageUrl(downloadUrl);
                imageRepository.save(savedImage);

                // Make ImageDto to return file name and url
                ImageDto imageDto = getImageDto(savedImage);
                imageDtos.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageDtos;
    }

    private ImageDto getImageDto(Image savedImage) {
        ImageDto imageDto = new ImageDto();
        imageDto.setFileName(savedImage.getFileName());
        imageDto.setImageId(savedImage.getId());
        imageDto.setImageUrl(savedImage.getImageUrl());
        return imageDto;
    }

    private String getDownloadUrl(Image savedImage) {
        String downloadUrl = IMAGE_URL_PATH + savedImage.getId();
        return downloadUrl;
    }

    private Image createImage(MultipartFile file) throws SerialException, SQLException, IOException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImageData(new SerialBlob(file.getBytes()));
        return image;
    }

    @Override
    public Image updateImage(MultipartFile imageFile, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(imageFile.getName());
            image.setFileType(imageFile.getContentType());
            image.setImageData(new SerialBlob(imageFile.getBytes()));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return image;
    }

}
