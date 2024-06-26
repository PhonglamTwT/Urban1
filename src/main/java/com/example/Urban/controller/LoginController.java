package com.example.Urban.controller;

import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.LoginDTO;
import com.example.Urban.service.EmployeeService;
import com.example.Urban.service.LoginService;
import com.example.Urban.service.TokenCacheService;
import org.apache.catalina.User;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/public")
public class LoginController {
    @Autowired
    private EmployeeService EmployeeService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private TokenCacheService tokenCacheService;


    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody AccountDTO loginRequest){
        LoginDTO result = loginService.loginJwt(loginRequest);
        String cacheJwt = tokenCacheService.getJwtFromCache(loginRequest);
        System.out.println("JWT from cache: " + cacheJwt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
