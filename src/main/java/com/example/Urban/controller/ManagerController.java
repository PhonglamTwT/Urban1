package com.example.Urban.controller;


import com.example.Urban.dto.ReqRes;
import com.example.Urban.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private EmployeeService EmployeeService;

//--------------------------------------------------------------------------------------------------

    @PostMapping("/addEmploy")
    public ResponseEntity<ReqRes> addEmployeeAndAccountJwt(@RequestBody ReqRes reg){
        return ResponseEntity.ok(EmployeeService.createEmployeeAndAccountJwt(reg));
    }
    @GetMapping("/showEmploy")
    public ResponseEntity<ReqRes> getAllEmployeeJwt(){
        return ResponseEntity.ok(EmployeeService.getAllEmployeeJwt());
    }
    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<ReqRes> deleteEmployeeJwt(@RequestParam int employeeId){
        return ResponseEntity.ok(EmployeeService.deleteEmployeeJwt(employeeId));
    }
    @PutMapping("/updateEmployee")
    public ResponseEntity<ReqRes> updateEmployeeJwt(@RequestParam int employeeId , @RequestParam MultipartFile file, @RequestParam String name,
                                                    @RequestParam String email, @RequestParam String phone, @RequestParam String gender,
                                                    @RequestParam String address, @RequestParam String position, @RequestParam String headquarter,
                                                    @RequestParam String username, @RequestParam String password, @RequestParam String role){
        return ResponseEntity.ok(EmployeeService.updateEmployeeJwt(employeeId, file, name, email, phone, gender, address, position, headquarter, username, password, role ));
    }

}
