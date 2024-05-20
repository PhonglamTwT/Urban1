package com.example.Urban.service;

import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.ForgotPasswordEntity;

public interface ForgotPasswordService {
    public String saveOTP(ForgotPasswordEntity forgotPassword);

    public ForgotPasswordEntity findOtpByAccount(AccountEntity account);

    public String deleteOTP(ForgotPasswordEntity forgotPassword);
}
