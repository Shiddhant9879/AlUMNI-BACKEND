package com.alumni.alumni_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "alumni_profiles")
public class AlumniProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 CHANGE IS HERE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Academic
    private String name;
    private String prn;
    private String btechBranch;
    private Integer passingYear;
    private Double cgpa;

    // Professional
    private String industry;
    private Integer experienceYears;
    private String currentCompany;
    private String phoneNumber;

    @Column(length = 500)
    private String skills;

    @Column(length = 500)
    private String achievements;
}
