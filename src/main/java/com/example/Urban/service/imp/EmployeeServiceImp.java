package com.example.Urban.service.imp;

import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.dto.ReqRes;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.repository.AccountRepository;
import com.example.Urban.repository.EmployeeRepository;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeEntity;

    @Override
    public ReqRes getAllEmployeeJwt() {
        ReqRes reqRes = new ReqRes();
        try {
            List<EmployeeEntity> result = employeeRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setEmployeeList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ReqRes getAllAccount() {
        ReqRes reqRes = new ReqRes();
        try {
            List<AccountEntity> result = accountRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setAccountList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ReqRes updateEmployeeJwt(int employeeId, MultipartFile file, String name, String email, String phone, String gender, String address, String position, String headquarter, String username, String password, String role) {
        ReqRes reqRes = new ReqRes();
        try {
            fileStorageService.save(file);
            Optional<EmployeeEntity> empOptional = employeeRepository.findById(employeeId);
            if (empOptional.isPresent()) {
                EmployeeEntity employee = empOptional.get();
                String existPhoto = employee.getImage();
                if(!existPhoto.equals(file.getOriginalFilename())){
                    fileStorageService.deleleEmployeePhoto(existPhoto);
                }
                employee.setImage(file.getOriginalFilename());
                employee.setName(name);
                employee.setEmail(email);
                employee.setPhone(phone);
                employee.setGender(gender);
                employee.setAddress(address);
                employee.setPosition(position);
                employee.setHeadquarter(headquarter);

                AccountEntity existingAccount = employee.getAccount();
                existingAccount.setUsername(username);
                existingAccount.setRole(role);
                if (password != null && !password.isEmpty()) {
                    existingAccount.setPassword(passwordEncoder.encode(password));
                }
                EmployeeEntity saveEmp = employeeRepository.save(employee);
                AccountEntity savedAcount = accountRepository.save(existingAccount);
                reqRes.setEmployee(saveEmp);
                reqRes.setAccount(savedAcount);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ReqRes deleteEmployeeJwt(int employeeId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<EmployeeEntity> empOptional = employeeRepository.findById(employeeId);
            if (empOptional.isPresent()) {
                EmployeeEntity employee = empOptional.get();
                AccountEntity account = employee.getAccount();
                accountRepository.delete(account);
                fileStorageService.deleleEmployeePhoto(employee.getImage());
                employeeRepository.delete(employee);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ReqRes createEmployeeAndAccountJwt(ReqRes createaccountRequest) {
        ReqRes resp = new ReqRes();
        try {
            EmployeeEntity emp = new EmployeeEntity();
            emp.setImage(createaccountRequest.getImage());
            emp.setName(createaccountRequest.getName());
            emp.setEmail(createaccountRequest.getEmail());
            emp.setPhone(createaccountRequest.getPhone());
            emp.setGender(createaccountRequest.getGender());
            emp.setAddress(createaccountRequest.getAddress());
            emp.setPosition(createaccountRequest.getPosition());
            emp.setHeadquarter(createaccountRequest.getHeadquarter());
            EmployeeEntity employeeResult = employeeEntity.save(emp);

            AccountEntity account = new AccountEntity();
            account.setUsername(createaccountRequest.getUsername());
            account.setRole(createaccountRequest.getRole());
            account.setPassword(passwordEncoder.encode(createaccountRequest.getPassword()));
            account.setEmployee(employeeResult);
            AccountEntity accountResult = accountRepository.save(account);
            if (accountResult.Getid() > 0 && employeeResult.Getid() > 0) {
                resp.setAccount((accountResult));
                resp.setEmployee(employeeResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
}
