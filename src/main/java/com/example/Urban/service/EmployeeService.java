package com.example.Urban.service;
import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.dto.EmployeeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EmployeeService {

    public EmployeeDTO getOneEmployeeJwt(int employeeId);

    List<EmployeeDTO> getAllEmployeeJwt();


    List<EmployeeDTO> getByDay (LocalDate day);

    List<AccountDTO> getAllAccount();

    EmployeeAccountDTO updateEmployeeJwt(int employeeId, MultipartFile file, String name, String email, String phone, String gender, String address,
                                         String position, String headquarter);

    String deleteEmployeeJwt(int employeeId);

    String createEmployeeAndAccountJwt(MultipartFile file, EmployeeAccountDTO createaccountRequest);

    EmployeeEntity checkEmail(String email);

    String changePassword(String email, String password);

    String changePasswordProactive(int employeeId, String newPassword, String oldPassword);
}
