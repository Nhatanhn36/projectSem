package com.example.productmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class ImageService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    public String uploadImage(MultipartFile imageFile) throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        Path absoluteImagePath = Paths.get(uploadDir, fileName); // Đã thay đổi đường dẫn

        // Đảm bảo thư mục tải lên đã tồn tại trước khi tải lên ảnh
        Files.createDirectories(absoluteImagePath.getParent());

        Files.copy(imageFile.getInputStream(), absoluteImagePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
