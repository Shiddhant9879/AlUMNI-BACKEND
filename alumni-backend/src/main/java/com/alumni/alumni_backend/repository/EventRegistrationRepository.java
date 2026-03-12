package com.alumni.alumni_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alumni.alumni_backend.model.EventRegistration;

import java.util.List;

public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {

    boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);

    List<EventRegistration> findByUser_Id(Long userId);

    List<EventRegistration> findByEvent_Id(Long eventId);
}
