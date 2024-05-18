package com.example.Urban.service.imp;

import com.example.Urban.controller.ManagerController;
import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.dto.ReqRes;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.repository.AccountRepository;
import com.example.Urban.repository.EmployeeRepository;
import com.example.Urban.repository.EventRepository;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EventRepository eventRepository;
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


                // Với mỗi nhân viên, thiết lập URL hình ảnh nếu có
                result.forEach(employee -> {
                    String filename = employee.getImage(); // Giả sử trường image là tên file lưu trữ
                    if (filename != null && !filename.isEmpty()) {
                        String imageUrl = MvcUriComponentsBuilder
                                .fromMethodName(ManagerController.class, "getFile", filename)
                                .build()
                                .toString();
                        employee.setImage(imageUrl);
                    }
                });

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
    public EmployeeDTO getEmployee(String name,String headquarter,String position,Date day)
    {
        Optional<EmployeeEntity> employeeOptional = employeeRepository.searchEmployee(name, headquarter, position, day);
        if (employeeOptional.isPresent()) {
            EmployeeEntity employee = employeeOptional.get();
            int userId = employeeRepository.findByname(name);

            EmployeeDTO dto = new EmployeeDTO();

            dto.setName(employee.getName());
            dto.setEmail(employee.getEmail());
            dto.setPhone(employee.getPhone());
            dto.setPosition(employee.getPosition());
            dto.setHeadquarter(employee.getHeadquarter());
            dto.setStatus(eventRepository.existsByEmployeeIdAndDay(userId,day));
            return dto;
        }else{
            return null;
        }
    }

    @Override
    public List<EmployeeDTO> getByDay (Date day)
    {
        List<EmployeeEntity> employees = employeeRepository.getByDay(day);
        if(!employees.isEmpty()){
            List<EmployeeDTO> dtos = new ArrayList<>(employees.size());
            for(EmployeeEntity employee : employees){
                int userId = employeeRepository.findByname(employee.getName());
                EmployeeDTO dto = new EmployeeDTO();

                dto.setName(employee.getName());
                dto.setName(employee.getName());
                dto.setEmail(employee.getEmail());
                dto.setPhone(employee.getPhone());
                dto.setPosition(employee.getPosition());
                dto.setHeadquarter(employee.getHeadquarter());
                dto.setStatus(eventRepository.existsByEmployeeIdAndDay(userId, day));

                dtos.add(dto);
            }
            return dtos;
        }
        return Collections.emptyList();
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

        String originalFilename = file.getOriginalFilename();
        // Thay thế khoảng trắng bằng dấu gạch nối
        String sanitizedFilename = originalFilename.replace(" ", "-");

        try {
            fileStorageService.save(file);
            Optional<EmployeeEntity> empOptional = employeeRepository.findById(employeeId);
            if (empOptional.isPresent()) {
                EmployeeEntity employee = empOptional.get();
                String existPhoto = employee.getImage();
                if(!existPhoto.equals(file.getOriginalFilename())){
                    fileStorageService.deleleEmployeePhoto(existPhoto);
                }
                employee.setImage(sanitizedFilename);
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
    public ReqRes createEmployeeAndAccountJwt(MultipartFile file, ReqRes createaccountRequest) {
        ReqRes resp = new ReqRes();

        fileStorageService.save(file);

        String originalFilename = file.getOriginalFilename();
        // Thay thế khoảng trắng bằng dấu gạch nối
        String sanitizedFilename = originalFilename.replace(" ", "-");

        try {
            EmployeeEntity emp = new EmployeeEntity();
            emp.setImage(sanitizedFilename);
            emp.setName(createaccountRequest.getName());
            emp.setEmail(createaccountRequest.getEmail());
            emp.setPhone(createaccountRequest.getPhone());
            emp.setGender(createaccountRequest.getGender());
            emp.setAddress(createaccountRequest.getAddress());
            emp.setPosition(createaccountRequest.getPosition());
            emp.setHeadquarter(createaccountRequest.getHeadquarter());

            EmployeeEntity employeeResult = employeeRepository.save(emp);

            AccountEntity account = new AccountEntity();
            account.setUsername(createaccountRequest.getUsername());
            account.setRole(createaccountRequest.getRole());
            account.setPassword(passwordEncoder.encode(createaccountRequest.getPassword()));
            account.setEmployee(employeeResult);
            AccountEntity accountResult = accountRepository.save(account);

            if (accountResult.Getid() > 0 && employeeResult.getId() > 0) {
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
