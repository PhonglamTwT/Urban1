package com.example.Urban.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {

    public void init();
    public void save(MultipartFile file);
    public Resource load(String filename);

    public void deleleEmployeePhoto(String fileName);
}
