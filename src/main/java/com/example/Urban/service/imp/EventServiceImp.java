package com.example.Urban.service.imp;

import com.example.Urban.dto.EventDTO;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import com.example.Urban.entity.EventEntity;
import com.example.Urban.repository.EmployeeRepository;
import com.example.Urban.repository.EventRepository;
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

    @Override
    public List<EventEntity> getAllEvent() {
        return List.of();
    }

    @Transactional
    public ReqRes createEvent(EventDTO eventDTO){
//        EventEntity event = new EventEntity();
//        event.setId(eventDTO.getId());
//        event.setName(eventDTO.getName());
//        event.setWorktime(eventDTO.getWorktime());
//        event.setWorkplace(eventDTO.getWorkplace());
//        event.setDay(eventDTO.getDay());
//        event.setStatus(eventDTO.getStatus());
//        eventRepository.save(event);
//        return event;
        ReqRes resp = new ReqRes();
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

                eventRepository.save(event);
                resp.setMessage("Event add successfully");
                resp.setStatusCode(200);
            }
            resp.setMessage("User not found");
            resp.setStatusCode(404);

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

//    @Override
//    public List<EventEntity> getAllEvent() { return eventRepository.findAll(); }
//    @Override
//    public ReqRes updateEvent(EventDTO eventDTO) {
////        EventEntity event = eventRepository.findById(eventDTO.getId()).orElseThrow(() -> new RuntimeException("Event not found"));
////        event.setName(eventDTO.getName());
////        event.setDay(eventDTO.getDay());
////        event.setWorktime(eventDTO.getWorktime());
////        event.setWorkplace(eventDTO.getWorkplace());
////        event.setStatus(eventDTO.getStatus());
////        try{
////            eventRepository.save(event);
////            return true;
////        }
////        catch (Exception e){
////            System.out.println("ManagerServiceImp updateEvent Error: " + e.getLocalizedMessage());
////            return false;
////        }
//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<EventEntity> eventOptional = eventRepository.findById(eventDTO.getId());
//            if (eventOptional.isPresent()) {
//                EventEntity event = eventOptional.get();
//                event.setName(eventDTO.getName());
//                event.setDay(eventDTO.getDay());
//                event.setWorktime(eventDTO.getWorktime());
//                event.setWorkplace(eventDTO.getWorkplace());
//                event.setStatus(eventDTO.getStatus());
//
//                EventEntity saveEvent = eventRepository.save(event);
//                reqRes.setStatusCode(200);
//                reqRes.setMessage("Event updated successfully");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessage("Event not found for update");
//            }
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessage("Error occurred while updating Event: " + e.getMessage());
//        }
//        return reqRes;
//    }
//    @Override
//    public ReqRes deleteEvent(int eventId){
//        ReqRes reqRes = new ReqRes();
//        try {
//            Optional<EventEntity> eventOptional = eventRepository.findById(eventId);
//            if (eventOptional.isPresent()) {
//                EventEntity event = eventOptional.get();
//                eventRepository.delete(event);
//                reqRes.setStatusCode(200);
//                reqRes.setMessage("Event deleted successfully");
//            } else {
//                reqRes.setStatusCode(404);
//                reqRes.setMessage("Event not found for deletion");
//            }
//        } catch (Exception e) {
//            reqRes.setStatusCode(500);
//            reqRes.setMessage("Error occurred while deleting event: " + e.getMessage());
//        }
//        return reqRes;
//    }

}
