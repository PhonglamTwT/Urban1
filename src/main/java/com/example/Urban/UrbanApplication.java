package com.example.Urban;


import com.example.Urban.service.FileStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class UrbanApplication implements CommandLineRunner {

	@Resource
	FileStorageService fileStorageService;

	public static void main(String[] args) {
		SpringApplication.run(UrbanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileStorageService.init();
	}

}