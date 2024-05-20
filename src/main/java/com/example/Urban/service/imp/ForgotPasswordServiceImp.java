package com.example.Urban.service.imp;

import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.ForgotPasswordEntity;
import com.example.Urban.repository.ForgotPasswordRepository;
import com.example.Urban.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordServiceImp implements ForgotPasswordService {
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public String saveOTP(ForgotPasswordEntity forgotPassword) {
        try {
            forgotPasswordRepository.save(forgotPassword);
            return "Save OTP Success";
        }
        catch (Exception e){
            throw new RuntimeException("Save OTP fail" + e.getLocalizedMessage());
        }
    }

    @Override
    public ForgotPasswordEntity findOtpByAccount(AccountEntity account) {
        ForgotPasswordEntity fp = forgotPasswordRepository.findByAccount(account).orElseThrow(()-> new RuntimeException("OTP not found by the Account"));
        return fp;
    }

    @Override
    public String deleteOTP(ForgotPasswordEntity forgotPassword) {
        try{
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            return "Delete OPT Success";
        }catch (Exception e){
            throw new RuntimeException("Delete OTP fail"+e.getLocalizedMessage());
        }

    }
}
