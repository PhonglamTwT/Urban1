package com.example.Urban.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventDTO {
    private int employee_id;
    private int id;
    private String name;
    private Date day;
    private String worktime;
    private String workplace;
    private String status;
    private String note;
}
