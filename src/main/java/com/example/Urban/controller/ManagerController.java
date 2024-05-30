package com.example.Urban.controller;

import com.example.Urban.dto.ChangePassword;
import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.dto.MailBody;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.entity.ForgotPasswordEntity;
import com.example.Urban.repository.ForgotPasswordRepository;
import com.example.Urban.service.EmailService;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.FileStorageService;
import com.example.Urban.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("/employee")
public class ManagerController {
    @Autowired
    private EmployeeService EmployeeService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private EmailService emailService;
//--------------------------------------------------------------------------------------------------

    @PostMapping("/addEmploy")
    public ResponseEntity<String> addEmployeeAndAccountJwt(@RequestParam("file") MultipartFile file, @ModelAttribute EmployeeAccountDTO empAcc){
        String result = EmployeeService.createEmployeeAndAccountJwt(file,empAcc);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/showEmploy")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeeJwt(){
        List<EmployeeDTO> employees = EmployeeService.getAllEmployeeJwt();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping("/showOneEmploy")
    public ResponseEntity<EmployeeDTO> getOneEmployeeJwtById(@RequestParam int employeeId){
        EmployeeDTO employees = EmployeeService.getOneEmployeeJwt(employeeId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


    @Autowired
    private FileStorageService fileStorageService;
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Resource file = fileStorageService.load(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG) // Hoặc định dạng phù hợp với loại hình ảnh của bạn
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<String> deleteEmployeeJwt(@RequestParam int employeeId){
        String result = EmployeeService.deleteEmployeeJwt(employeeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateEmployee")
    public ResponseEntity<EmployeeAccountDTO> updateEmployeeJwt(@RequestParam int employeeId , @RequestParam MultipartFile file, @RequestParam String name,
                                                    @RequestParam String email, @RequestParam String phone, @RequestParam String gender,
                                                    @RequestParam String address, @RequestParam String position, @RequestParam String headquarter,
                                                    @RequestParam String username, @RequestParam String password, @RequestParam String role){
        EmployeeAccountDTO employeeAccountDTO = EmployeeService.updateEmployeeJwt(employeeId, file, name, email, phone, gender, address, position, headquarter, username, password, role );
        return new ResponseEntity<>(employeeAccountDTO, HttpStatus.OK);
    }

    @GetMapping("/searchEmployee")
    public ResponseEntity<EmployeeDTO> searchEmployee(@RequestParam Map<String,String> params,
                                                      @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")  Date day){
        EmployeeDTO employeeDTO = EmployeeService.getEmployee(params.get("name"),params.get("headquarter"),params.get("position"),day);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }
    @GetMapping("/getByDate")
    public ResponseEntity<List<EmployeeDTO>> getByDate(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")  Date day){
        List<EmployeeDTO> employeeDTOS = EmployeeService.getByDay(day);
        return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam String email){
        System.out.printf("Email: "+email);
        EmployeeEntity employee = EmployeeService.checkEmail(email);
        ForgotPasswordEntity checkOTPExist = forgotPasswordService.findOtpByAccount(employee.getAccount());
        if(checkOTPExist!=null){
            System.out.println("Da xoa OTP");
            forgotPasswordService.deleteOTP(checkOTPExist);
        }
        int otp = otpGenerator();
        MailBody mailBody = new MailBody();
        mailBody.setTo(email);
        mailBody.setText("Your OTP for reset password " + otp);
        mailBody.setSubject("OTP");
        ForgotPasswordEntity fp = new ForgotPasswordEntity();
        fp.setOtp(otp);
        fp.setExpirationTime(generateExpirationTime(5));
        fp.setAccount(employee.getAccount());
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordService.saveOTP(fp);
        return ResponseEntity.ok("Email send successfully");
    }

    private Date generateExpirationTime(int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes); // Ví dụ: OTP sẽ hết hạn sau 15 phút
        return calendar.getTime();
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<String> verifyOTP(@RequestParam int otp, @RequestParam String email){
        EmployeeEntity employee = EmployeeService.checkEmail(email);
        ForgotPasswordEntity fp = forgotPasswordService.findOtpByAccount(employee.getAccount());
        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordService.deleteOTP(fp);
            return new ResponseEntity<>("OTP expired",HttpStatus.EXPECTATION_FAILED);
        } else if (fp.getOtp()==otp) {
            return new ResponseEntity<>("OTP verified",HttpStatus.OK);
        }
        return new ResponseEntity<>("OTP verified fail",HttpStatus.EXPECTATION_FAILED);
    }

    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@ModelAttribute ChangePassword changePassword, @RequestParam String email){
        if(!Objects.equals(changePassword.getChangePassword(), changePassword.getRepeatPassword())){
            return new ResponseEntity<>("Password not equal",HttpStatus.EXPECTATION_FAILED);
        }
        String result = EmployeeService.changePassword(email,changePassword.getChangePassword());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
