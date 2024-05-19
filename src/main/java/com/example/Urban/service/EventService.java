package com.example.Urban.service;

import com.example.Urban.dto.EventDTO;
import com.example.Urban.entity.EventEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EventService {
    public List<EventEntity> getAllEvent();
    //public List<EventDTO> getByEmployee(int id);

//    public ReqRes updateEvent(EventDTO eventDTO);
//    public ReqRes deleteEvent(int employeeId);
//    public ReqRes createEvent(EventDTO eventDTO);
}
