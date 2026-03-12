package com.alumni.alumni_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alumni.alumni_backend.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 🔍 Search bar
    List<Event> findByTitleContainingIgnoreCase(String keyword);

    // 📅 Upcoming events
    List<Event> findByEventDateAfter(LocalDate today);

    // 📍 Nearest events (sorted)
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate today);
}
