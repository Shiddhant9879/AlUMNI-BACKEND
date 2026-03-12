package com.alumni.alumni_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alumni.alumni_backend.model.Event;
import com.alumni.alumni_backend.model.EventRegistration;
import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.repository.EventRegistrationRepository;
import com.alumni.alumni_backend.repository.EventRepository;
import com.alumni.alumni_backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventRegistrationService(
            EventRegistrationRepository registrationRepository,
            UserRepository userRepository,
            EventRepository eventRepository) {

        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // =========================
    // REGISTER USER FOR EVENT
    // =========================
    @Transactional
    public void registerUserForEvent(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Event not found"));

        if (registrationRepository.existsByUser_IdAndEvent_Id(userId, eventId)) {
            throw new IllegalStateException("User already registered for this event");
        }

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setEmail(user.getEmail());
        registration.setPhone(user.getPhone());
        registration.setRegisteredAt(LocalDateTime.now());

        registrationRepository.save(registration);
    }

    // =========================
    // CHECK IF USER REGISTERED
    // =========================
    public boolean isUserRegistered(Long userId, Long eventId) {
        return registrationRepository.existsByUser_IdAndEvent_Id(userId, eventId);
    }

    // =========================
    // GET REGISTRATIONS BY USER
    // =========================
    public List<EventRegistration> getRegistrationsForUser(Long userId) {
        return registrationRepository.findByUser_Id(userId);
    }

    // =========================
    // ADMIN: GET ALL REGISTRATIONS
    // =========================
    public List<EventRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
