package com.example.Urban.dto;

import lombok.Data;

import javax.print.DocFlavor;

@Data
public class LoginDTO {
    private String token;
    private String message;
    private String role;
    private int employeeId;
}
