package com.truckbooking.service.impl;

import com.truckbooking.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Allowed image types
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png"
    );

    @Override
    public String uploadTruckImage(MultipartFile file) {
        return uploadImage(file, "trucks");
    }

    @Override
    public String uploadDriverImage(MultipartFile file) {
        return uploadImage(file, "drivers");
    }

    // Common Method
    private String uploadImage(MultipartFile file, String folderName) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please select an image to upload.");
        }

        System.out.println("Content Type: " + file.getContentType());
        System.out.println("Original File Name: " + file.getOriginalFilename());

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Only JPG, JPEG and PNG images are allowed.");
        }

        try {

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

            String extension = "";

            int index = originalFileName.lastIndexOf(".");

            if (index > 0) {
                extension = originalFileName.substring(index).toLowerCase();
            }

            if (!extension.equals(".jpg")
                    && !extension.equals(".jpeg")
                    && !extension.equals(".png")) {

                throw new RuntimeException("Only JPG, JPEG and PNG images are allowed.");
            }

            String fileName = UUID.randomUUID() + extension;

            Path uploadPath = Paths.get(uploadDir, folderName);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    file.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "uploads/" + folderName + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Unable to upload image.");
        }
    }
}