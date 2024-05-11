package com.example.Urban.service;

import com.example.Urban.dto.ReqRes;

public interface LoginService {
    ReqRes loginJwt(ReqRes loginRequest);
}
