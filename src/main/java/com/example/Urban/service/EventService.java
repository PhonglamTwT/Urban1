package com.example.Urban.service;

import com.example.Urban.dto.EventDTO;
import com.example.Urban.entity.EventEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EventService {
    public List<EventEntity> getAllEvent();
    public List<EventEntity> getByEmployee(int id);

    public String updateEvent(EventDTO eventDTO);
    public String deleteEvent(int employeeId);
    public String createEvent(EventDTO eventDTO);
}
