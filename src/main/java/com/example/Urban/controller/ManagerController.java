package com.example.Urban.controller;

import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.dto.EmployeeAccountDTO;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private EmployeeService EmployeeService;

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
        return ResponseEntity.ok(EmployeeService.getEmployee(params.get("name"),params.get("headquarter"),params.get("position"),day));
    }
    @GetMapping("/getByDate")
    public ResponseEntity<List<EmployeeDTO>> getByDate(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")  Date day){
        return ResponseEntity.ok(EmployeeService.getByDay(day));
    }

}
