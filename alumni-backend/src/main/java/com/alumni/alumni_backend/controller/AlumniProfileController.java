package com.alumni.alumni_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.alumni.alumni_backend.dto.AlumniDirectoryDto;
import com.alumni.alumni_backend.model.AlumniProfile;
import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.service.AlumniProfileService;
import com.alumni.alumni_backend.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class AlumniProfileController {

    private final AlumniProfileService profileService;
    private final UserService userService;

    public AlumniProfileController(
            AlumniProfileService profileService,
            UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    // ==================================================
    // ADMIN ONLY – CREATE / UPDATE ALUMNI PROFILE
    // ==================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AlumniProfile saveProfile(
            Authentication authentication,
            @RequestBody AlumniProfile profile) {

        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        if (profileService.profileExists(user.getId())) {
            return profileService.updateProfile(user, profile);
        }

        return profileService.createProfile(user, profile);
    }

    // ==================================================
    // ADMIN ONLY – VIEW OWN PROFILE (OPTIONAL)
    // ==================================================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AlumniProfile getMyProfile(Authentication authentication) {

        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        return profileService.getProfileByUserId(user.getId());
    }

    // ==================================================
    // DIRECTORY – ALL AUTHENTICATED USERS
    // ==================================================
    @GetMapping("/directory")
    public Page<AlumniDirectoryDto> getAlumniDirectory(Pageable pageable) {
        return profileService.getDirectory(pageable);
    }
}
