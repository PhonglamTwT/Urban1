package com.example.Urban.service;

import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.dto.EmployeeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployeeJwt();

    List<AccountDTO> getAllAccount();

    EmployeeAccountDTO updateEmployeeJwt(int employeeId, MultipartFile file, String name, String email, String phone, String gender, String address,
                                         String position, String headquarter, String username, String password, String role);

    String deleteEmployeeJwt(int employeeId);

    String createEmployeeAndAccountJwt(MultipartFile file, EmployeeAccountDTO createaccountRequest);
}
