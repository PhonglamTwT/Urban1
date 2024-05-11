package com.example.Urban.controller;


import com.example.Urban.dto.ReqRes;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<ReqRes> addEmployeeAndAccountJwt(@RequestParam MultipartFile file, @ModelAttribute ReqRes reg){

        return ResponseEntity.ok(EmployeeService.createEmployeeAndAccountJwt(file, reg));
    }


    @GetMapping("/showEmploy")
    public ResponseEntity<ReqRes> getAllEmployeeJwt(){
        return ResponseEntity.ok(EmployeeService.getAllEmployeeJwt());
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
