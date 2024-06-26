package com.example.Urban.service.imp;

import com.example.Urban.controller.ManagerController;
import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.entity.ForgotPasswordEntity;
import com.example.Urban.repository.AccountRepository;
import com.example.Urban.repository.EmployeeRepository;
import com.example.Urban.repository.EventRepository;
import com.example.Urban.repository.ForgotPasswordRepository;
import com.example.Urban.service.EmployeeMapper;
import com.example.Urban.service.EmployeeNotFoundException;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private EmployeeMapper employeeMapper;



    @Override
    public EmployeeDTO getOneEmployeeJwt(int employeeId) {
        EmployeeEntity employees = employeeRepository.findById(employeeId).orElseThrow(()->new RuntimeException("Cant find employee"));

        EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(employees);

        // Thiết lập URL hình ảnh cho mỗi nhân viên nếu có

            String filename = employeeDTO.getImage(); // Giả sử trường image là tên file lưu trữ
            if (filename != null && !filename.isEmpty()) {
                String imageUrl = MvcUriComponentsBuilder
                        .fromMethodName(ManagerController.class, "getFile", filename)
                        .build()
                        .toString();
                employeeDTO.setImage(imageUrl);
            }
//            String account = employees.getAccount().getUsername();
//            if (account.isEmpty()){
//                employeeDTO.setUsername("null");
//            }
//            else {
//                employeeDTO.setUsername(account);
//            }
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeJwt() {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        employees.forEach(employee -> System.out.println("Employee ID: " + employee.getId()));

        List<EmployeeDTO> employeeDTOs = employeeMapper.toEmployeeDTOs(employees);
        employeeDTOs.forEach(dto -> System.out.println("DTO ID: " + dto.getId()));

        // Thiết lập URL hình ảnh cho mỗi nhân viên nếu có
        employeeDTOs.forEach(employeeDTO -> {
            String filename = employeeDTO.getImage(); // Giả sử trường image là tên file lưu trữ
            if (filename != null && !filename.isEmpty()) {
                String imageUrl = MvcUriComponentsBuilder
                        .fromMethodName(ManagerController.class, "getFile", filename)
                        .build()
                        .toString();
                employeeDTO.setImage(imageUrl);
            }
            employees.stream()
                    .filter(employee -> employee.getId() == employeeDTO.getId())
                    .findFirst()
                    .ifPresent(employee ->{
                        String temp = Optional.ofNullable(employee.getAccount())
                                .map(account -> account.getUsername())
                                .filter(username -> !username.isEmpty())
                                .orElse("null");
                        employeeDTO.setUsername(temp);
                    });
        });
        return employeeDTOs;
    }


    @Override
    public List<EmployeeDTO> getByDay (LocalDate day)
    {
//        System.out.printf("day: " + day);
        List<EmployeeEntity> employees = employeeRepository.getByDay(day);
//        System.out.printf("em: " + employees);
        if(!employees.isEmpty()){
            List<EmployeeDTO> dtos = new ArrayList<>(employees.size());
            for(EmployeeEntity employee : employees){
//                int userId = employeeRepository.findByname(employee.getName());
                EmployeeDTO dto = new EmployeeDTO();

                dto.setId(employee.getId());




                // Thiết lập URL hình ảnh cho mỗi nhân viên nếu có

                    String filename = employee.getImage(); // Giả sử trường image là tên file lưu trữ
                    if (filename != null && !filename.isEmpty()) {
                        String imageUrl = MvcUriComponentsBuilder
                                .fromMethodName(ManagerController.class, "getFile", filename)
                                .build()
                                .toString();
                        dto.setImage(imageUrl);
                    }

//                dto.setImage(employee.getImage());



                dto.setName(employee.getName());
                dto.setEmail(employee.getEmail());
                dto.setPhone(employee.getPhone());
                dto.setGender(employee.getGender());
                dto.setAddress(employee.getAddress());
                dto.setPosition(employee.getPosition());
                dto.setHeadquarter(employee.getHeadquarter());
//                dto.setStatus(eventRepository.existsByEmployeeIdAndDay(userId, day));

                dtos.add(dto);
            }
            return dtos;
        }
        return Collections.emptyList();
    }
    @Override
    public List<AccountDTO> getAllAccount() {
        List<AccountEntity> account= accountRepository.findAll();
        List<AccountDTO> accountDTOS = employeeMapper.toAccountDTOs(account);

        return accountDTOS;
    }

    @Override
    public EmployeeAccountDTO updateEmployeeJwt(int employeeId, MultipartFile file, String name, String email, String phone, String gender, String address, String position, String headquarter) {


        String sanitizedFilename = "";

//        String sanitizedFilename = saveFile(file);
        Optional<EmployeeEntity> empOptional = employeeRepository.findById(employeeId);
        if (empOptional.isPresent()) {
            EmployeeEntity employee = empOptional.get();

            if(file == null){
                sanitizedFilename = employee.getImage();
            }
            else{
                if (!employee.getImage().equals(file.getOriginalFilename())) {
                    fileStorageService.deleleEmployeePhoto(employee.getImage());
                    sanitizedFilename = saveFile(file);
                }else{
                    sanitizedFilename = employee.getImage();
                }
            }
            if(!employee.getEmail().equals(email)){
                if(employeeRepository.findByEmail(email).isPresent()){
                    throw new RuntimeException("Email exist");
                }
            }
            updateEmployeeDetails(employee, sanitizedFilename, name, email, phone, gender, address, position, headquarter);

            AccountEntity existingAccount = employee.getAccount();

            employeeRepository.save(employee);
            accountRepository.save(existingAccount);

            return createEmployeeAccountDTO(employee, existingAccount);

//            EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(employee);
//            employeeDTO.setAccount(employeeMapper.toAccountDTO(existingAccount));
//            return employeeDTO;
        } else {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
    }


    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Override
    public String deleteEmployeeJwt(int employeeId) {
        Optional<EmployeeEntity> empOptional = employeeRepository.findById(employeeId);
        if (empOptional.isPresent()) {
            EmployeeEntity employee = empOptional.get();
            AccountEntity account = employee.getAccount();

            ForgotPasswordEntity forgotPassword = employee.getAccount().getForgotPassword();

            if (forgotPassword != null) {
                forgotPasswordRepository.delete(forgotPassword);
            }
            fileStorageService.deleleEmployeePhoto(employee.getImage());
            employeeRepository.delete(employee);
            return "Employee deleted successfully";
        } else {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

    }

//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<EmployeeEntity> empOptional = employeeRepository.findById(employeeId);
//            if (empOptional.isPresent()) {
//                EmployeeEntity employee = empOptional.get();
//                AccountEntity account = employee.getAccount();
//                accountRepository.delete(account);
//                fileStorageService.deleleEmployeePhoto(employee.getImage());
//                employeeRepository.delete(employee);
//                reqRes.setStatusCode(200);
//                reqRes.setMessage("User deleted successfully");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessage("User not found for deletion");
//            }
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
//        }
//        return reqRes;
//    }

    @Override
    public String createEmployeeAndAccountJwt(MultipartFile file, EmployeeAccountDTO createAccountRequest) {
        if(accountRepository.findByUsername(createAccountRequest.getUsername()).isPresent()){
            throw new RuntimeException("Username exist");
        }
        if(employeeRepository.findByEmail(createAccountRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email exist");
        }
        String sanitizedFilename = saveFile(file);
        try {
            EmployeeEntity emp = new EmployeeEntity();
            emp.setImage(sanitizedFilename);
            emp.setName(createAccountRequest.getName());
            emp.setEmail(createAccountRequest.getEmail());
            emp.setPhone(createAccountRequest.getPhone());
            emp.setGender(createAccountRequest.getGender());
            emp.setAddress(createAccountRequest.getAddress());
            emp.setPosition(createAccountRequest.getPosition());
            emp.setHeadquarter(createAccountRequest.getHeadquarter());

            EmployeeEntity employeeResult = employeeRepository.save(emp);

            AccountEntity account = new AccountEntity();
            account.setUsername(createAccountRequest.getUsername());
            account.setRole(createAccountRequest.getRole());
            account.setPassword(passwordEncoder.encode(createAccountRequest.getPassword()));
            account.setEmployee(employeeResult);
            AccountEntity accountResult = accountRepository.save(account);

            if (accountResult.getId() > 0 && employeeResult.getId() > 0) {
                return "Employee create successfully";
            }
        }catch (Exception e){
            throw new RuntimeException("Can't create Employee " + e);
        }
        throw new RuntimeException("Can't create Employee " );
    }

    @Override
    public EmployeeEntity checkEmail(String email) {
        Optional<EmployeeEntity> employee = employeeRepository.findByEmail(email);
        if(employee.isPresent()){
            return employee.get();
        }
        throw new RuntimeException("Email not found");
    }

    @Override
    public String changePassword(String email, String password) {
        try{
            EmployeeEntity employee = employeeRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Employee with that email doesn't exist"));
            AccountEntity account = employee.getAccount();
            account.setPassword(passwordEncoder.encode(password));
            accountRepository.save(account);
            return "Change password success";
        }catch (Exception e){
            throw new RuntimeException("Change password fail"+e.getLocalizedMessage());
        }
    }

    @Override
    public String changePasswordProactive(int employeeId, String newPassword, String oldPassword) {
        EmployeeEntity emp = employeeRepository.findById(employeeId).orElseThrow(()->new RuntimeException("User not found"));
        if(passwordEncoder.matches(oldPassword,emp.getAccount().getPassword())){
            AccountEntity account = emp.getAccount();
            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
            return "Change password success";
        }
        throw new RuntimeException("Change password fail");
    }

    private void updateEmployeeDetails(EmployeeEntity employee, String image, String name, String email, String phone, String gender, String address, String position, String headquarter) {
//        employee.setId(employee.getId());
        employee.setImage(image);
        employee.setName(name);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setGender(gender);
        employee.setAddress(address);
        employee.setPosition(position);
        employee.setHeadquarter(headquarter);
    }

    private EmployeeAccountDTO createEmployeeAccountDTO(EmployeeEntity employee, AccountEntity account) {
        EmployeeAccountDTO dto = new EmployeeAccountDTO();
        dto.setImage(employee.getImage());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setGender(employee.getGender());
        dto.setAddress(employee.getAddress());
        dto.setPosition(employee.getPosition());
        dto.setHeadquarter(employee.getHeadquarter());

        dto.setUsername(account.getUsername());
        dto.setPassword(account.getPassword());
        dto.setRole(account.getRole());

        return dto;
    }
    private String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = originalFilename.replace(" ", "-");
        fileStorageService.save(file);
        return sanitizedFilename;
    }


}
