package com.alumni.alumni_backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alumni.alumni_backend.dto.EventRegistrationDto;
import com.alumni.alumni_backend.model.EventRegistration;
import com.alumni.alumni_backend.service.EventRegistrationService;

@RestController
@RequestMapping("/api/event-registrations")
@CrossOrigin
public class EventRegistrationController {

    private final EventRegistrationService registrationService;

    public EventRegistrationController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // 🎓 STUDENT → register for event
    @PostMapping
    public void register(@RequestBody EventRegistrationDto dto) {
        registrationService.registerUserForEvent(
                dto.getUserId(),
                dto.getEventId());
    }

    // 🧑‍💼 ADMIN → view all registrations
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EventRegistration> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }
}
