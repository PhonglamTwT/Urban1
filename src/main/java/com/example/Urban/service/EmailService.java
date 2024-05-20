package com.example.Urban.service;

import com.example.Urban.dto.MailBody;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {
    void sendSimpleMessage(MailBody mailBody);

}
