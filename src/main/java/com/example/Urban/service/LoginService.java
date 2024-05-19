package com.example.Urban.service;

import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.LoginDTO;
import org.apache.catalina.User;

public interface LoginService {
    LoginDTO loginJwt(AccountDTO loginRequest);
}
