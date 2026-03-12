package com.alumni.alumni_backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.alumni.alumni_backend.model.Event;
import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.service.EventService;
import com.alumni.alumni_backend.service.UserService;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(
            EventService eventService,
            UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    // 🔓 visible to all logged users
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // 🔐 ADMIN / FACULTY only
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @PostMapping
    public Event createEvent(
            @RequestBody Event event,
            Authentication authentication) {

        String email = authentication.getName(); // JWT se
        User creator = userService.getUserByEmail(email);

        return eventService.createEvent(creator, event);
    }

}
