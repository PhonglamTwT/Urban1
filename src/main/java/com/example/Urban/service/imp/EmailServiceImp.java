package com.example.Urban.service.imp;

import com.example.Urban.dto.MailBody;
import com.example.Urban.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.SequenceInputStream;

@Service
public class EmailServiceImp implements EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    public EmailServiceImp(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMessage(MailBody mailBody) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setFrom(email);
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());
        System.out.printf("To: "+ message.getTo()+"Form: "+email+"Subject: "+message.getSubject()+"Text: "+message.getText());
        javaMailSender.send(message);
    }
}
