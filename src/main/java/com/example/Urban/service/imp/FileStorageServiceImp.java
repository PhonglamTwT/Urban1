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
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path pathImage = root.resolve(filename);
            Resource resource = new UrlResource(pathImage.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Loi khong tim thay file hoac khong doc duoc file");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Loi khong tim thay file " + e.getMessage());
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
