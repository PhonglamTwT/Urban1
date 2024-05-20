package com.example.Urban.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MailBody{
    String to;
    String subject;
    String text;
}
