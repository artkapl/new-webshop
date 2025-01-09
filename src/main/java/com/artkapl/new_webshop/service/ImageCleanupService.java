package com.artkapl.new_webshop.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.artkapl.new_webshop.model.Image;
import com.artkapl.new_webshop.repository.ImageRepository;

@Service
public class ImageCleanupService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @Value("${upload.directory}")
    private String uploadDir;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)  // every 24 hours
    public void cleanOrphanedImages() throws IOException {
        List<Image> orphanedImages = imageRepository.findByIsOrphaned(true);

        for (Image image : orphanedImages) {
            String filePath = Paths.get(uploadDir)
                .resolve(String.valueOf(image.getProduct().getId()))
                .resolve(image.getImageUrl()
                .substring(image.getImageUrl().lastIndexOf("/") + 1)).toString();
            
            if (filePath != null) {
                imageService.deleteImageFromDisk(filePath);  // delete image from disk
            }
            imageRepository.delete(image);  // delete image from DB
        }
    }
}
