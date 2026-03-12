package com.alumni.alumni_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== EXISTING FIELDS (UNCHANGED) =====
    private String title;

    @Column(length = 500)
    private String description;

    private String eventType; // STARTUP, CODING, INDUSTRIAL

    private String location;

    private LocalDate eventDate;

    private LocalDate registrationDeadline;

    // ===== NEW FIELDS (ADDED FOR EVENT POSTING FLOW) =====

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // ADMIN who posted the event

    private LocalDateTime createdAt; // when event was created
}
