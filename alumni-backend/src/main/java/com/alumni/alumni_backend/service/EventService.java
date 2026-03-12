package com.alumni.alumni_backend.service;

import org.springframework.stereotype.Service;

import com.alumni.alumni_backend.model.Event;
import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * ❌ TEMPORARY EVENT CREATION
     * (Keep only for Postman / dev testing)
     */
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * ✅ SECURE EVENT CREATION (FINAL)
     * Used by Admin / Faculty via JWT
     */
    public Event createEvent(User creator, Event event) {

        if (!creator.getRole().equalsIgnoreCase("FACULTY")
                && !creator.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Only faculty or admin can create events");
        }

        // 🔐 set ownership & audit fields
        event.setCreatedBy(creator);
        event.setCreatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }

    /**
     * Get all events
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Get event by ID
     */
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    /**
     * Search events by title
     */
    public List<Event> searchEvents(String keyword) {
        return eventRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * Get upcoming events
     */
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDate.now());
    }

    /**
     * Get nearest events (sorted)
     */
    public List<Event> getNearestEvents() {
        return eventRepository
                .findByEventDateAfterOrderByEventDateAsc(LocalDate.now());
    }
}
