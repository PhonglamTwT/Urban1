package com.example.Urban.service.imp;

import com.example.Urban.dto.EventDTO;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.entity.EventEntity;
import com.example.Urban.repository.EmployeeRepository;
import com.example.Urban.repository.EventRepository;
import com.example.Urban.service.EmployeeNotFoundException;
import com.example.Urban.service.EventService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImp implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public String createEvent(EventDTO eventDTO){
        try {
            Optional<EmployeeEntity> employee = employeeRepository.findById(eventDTO.getEmployee_id());

            if(employee.isPresent()){
                EventEntity event = new EventEntity();
                event.setName(eventDTO.getName());
                event.setWorktime(eventDTO.getWorktime());
                event.setWorkplace(eventDTO.getWorkplace());
                event.setDay(eventDTO.getDay());
                event.setStatus(eventDTO.getStatus());
                event.setEmployee(employee.get());
                event.setNote(eventDTO.getNote());
                eventRepository.save(event);
                return "Event create successfully";
            }
            else {
                throw new EmployeeNotFoundException("Employee not found with id: " + eventDTO.getEmployee_id());
            }
        }catch (Exception e){
            throw new RuntimeException("Can't create Event " + e);
        }
    }

    @Override
    public List<EventEntity> getAllEvent() { return eventRepository.findAll(); }

    @Override
    public List<EventEntity> getByEmployee(int id) {
        return eventRepository.findByEmployeeId(id);
    }

    @Override
    public String updateEvent(EventDTO eventDTO) {

        try {
            Optional<EventEntity> eventOptional = eventRepository.findById(eventDTO.getId());
            if (eventOptional.isPresent()) {
                EventEntity event = eventOptional.get();
                event.setName(eventDTO.getName());
                event.setDay(eventDTO.getDay());
                event.setWorktime(eventDTO.getWorktime());
                event.setWorkplace(eventDTO.getWorkplace());
                event.setStatus(eventDTO.getStatus());

                EventEntity saveEvent = eventRepository.save(event);
                return "Event updated successfully";
            }
            else {
                throw new EmployeeNotFoundException("Event not found with id: " + eventDTO.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Update event fail");
        }
    }
    @Override
    public String deleteEvent(int eventId){
        Optional<EventEntity> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            EventEntity event = eventOptional.get();
            eventRepository.delete(event);
            return "Event deleted successfully";

        } else {
            throw new RuntimeException("Event not found with id: " + eventId);
        }

    }

}
