package com.example.Urban.controller;

import com.example.Urban.dto.ReqRes;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class LoginController {
    @Autowired
    private EmployeeService EmployeeService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(loginService.loginJwt(req));
    }
}
