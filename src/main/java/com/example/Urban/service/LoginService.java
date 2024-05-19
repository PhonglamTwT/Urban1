package com.example.Urban.service;

import com.example.Urban.dto.AccountDTO;
import org.apache.catalina.User;

public interface LoginService {
    String loginJwt(AccountDTO loginRequest);
}
