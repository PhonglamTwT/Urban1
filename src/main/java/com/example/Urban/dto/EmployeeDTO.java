package com.example.Urban.dto;
import com.example.Urban.entity.AccountEntity;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {

    private String image;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private String position;
    private String headquarter;


    public void setAccount(AccountDTO accountDTO) {

    }
}
