package com.example.Urban.dto;

import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;

    private String image;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private String position;
    private String headquarter;

    private String username;
    private String password;
    private String role;

    private String employee_id;
    private AccountEntity account;
    private EmployeeEntity employee;
    private List<AccountEntity> accountList;
    private List<EmployeeEntity> employeeList;

}