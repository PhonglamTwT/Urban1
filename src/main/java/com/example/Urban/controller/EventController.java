package com.example.Urban.controller;

import com.example.Urban.dto.EventDTO;
import com.example.Urban.entity.EventEntity;
import com.example.Urban.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

//    @GetMapping("/showEvent")
//    public ResponseEntity<List<EventEntity>> GetAllEvent() {
//        List<EventEntity> event = eventService.getAllEvent();
//        return ResponseEntity.ok().body(event);
//    }
//
//    @PostMapping("/addEvent")
//    public ResponseEntity<ReqRes> addEvent(@RequestBody EventDTO eventDTO) {
//        return ResponseEntity.ok(eventService.createEvent(eventDTO));
//    }
//
//    @PostMapping("/updateEvent")
//    public ResponseEntity<ReqRes> updateEmployee(@RequestBody EventDTO eventDTO){
//        return ResponseEntity.ok(eventService.updateEvent(eventDTO));
//    }
//    @DeleteMapping("/deleteEvent")
//    public ResponseEntity<ReqRes> deleteEvent(@RequestParam int eventId){
//        return ResponseEntity.ok(eventService.deleteEvent(eventId));
//    }
}
