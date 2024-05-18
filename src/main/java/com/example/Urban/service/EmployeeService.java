package com.example.Urban.service;

import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.dto.ReqRes;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;

import com.example.Urban.dto.EmployeeAccountDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface EmployeeService {

    ReqRes getAllEmployeeJwt();

    ReqRes getAllAccount();
    EmployeeDTO getEmployee(String name, String headquarter, String position, Date day);
    List<EmployeeDTO> getByDay (Date day);

    ReqRes updateEmployeeJwt(int employeeId, MultipartFile file, String name, String email, String phone, String gender, String address,
                                  String position, String headquarter, String username, String password, String role);

    ReqRes deleteEmployeeJwt(int employeeId);

    ReqRes createEmployeeAndAccountJwt(MultipartFile file, ReqRes createaccountRequest);
}
