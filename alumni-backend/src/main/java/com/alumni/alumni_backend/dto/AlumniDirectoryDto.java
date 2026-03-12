package com.alumni.alumni_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlumniDirectoryDto { // ✅ CAPITAL D + CAPITAL DTO

    private String name;
    private String role;
    private String program;
    private Integer passingYear;
    private String city;
    private String company;
    private String skills;
    private String email;
    private String phone;

}
