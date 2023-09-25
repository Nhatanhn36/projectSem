package com.example.productmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
public class ImageController {
    @Value("${image.upload.dir}")
    private String uploadDir;


    @GetMapping("/images/{imageFileName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageFileName,
                                               HttpServletRequest request) throws MalformedURLException {
        Path imagePath = Paths.get(uploadDir).resolve(imageFileName); // Đã thay đổi đường dẫn
        Resource resource = new UrlResource(imagePath.toUri());

        return ResponseEntity.ok()
                .header("Content-Type", request.getServletContext().getMimeType(imagePath.toString()))
                .body(resource);
    }
}


