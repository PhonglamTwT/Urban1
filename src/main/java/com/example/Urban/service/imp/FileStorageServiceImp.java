package com.example.Urban.service.imp;

import com.example.Urban.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageServiceImp implements FileStorageService {
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            // Thay thế khoảng trắng bằng dấu gạch nối
            String sanitizedFilename = originalFilename.replace(" ", "-");

            Files.copy(file.getInputStream(), this.root.resolve(sanitizedFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UrlResource load(String filename) {
        try {
            Path file = root.resolve(filename);  // Ensure 'root' is initialized somewhere in your class
            UrlResource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable: " + filename);
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error constructing URL for the file: " + filename, e);
        }
    }

    @Override
    public void deleleEmployeePhoto(String fileName) {
        Path imagePath = root.resolve(fileName);
        try {
            Files.deleteIfExists(imagePath);
            System.out.println("Delele Image Success: " + fileName);
        } catch (IOException e) {
            System.out.println("Delele Image Fail: " + fileName);
            e.printStackTrace();
        }
    }
}
