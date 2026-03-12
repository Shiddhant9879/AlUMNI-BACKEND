package com.alumni.alumni_backend.service;

import org.springframework.stereotype.Service;

import com.alumni.alumni_backend.dto.AlumniDirectoryDto;
import com.alumni.alumni_backend.model.AlumniProfile;
import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.repository.AlumniProfileRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AlumniProfileService {

    private final AlumniProfileRepository profileRepository;

    public AlumniProfileService(AlumniProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // ===============================
    // CREATE PROFILE (FIRST TIME)
    // ===============================
    public AlumniProfile createProfile(User user, AlumniProfile profile) {

        if (profileRepository.findByUser_Id(user.getId()).isPresent()) {
            throw new RuntimeException("Alumni profile already exists");
        }

        profile.setUser(user); // 🔐 foreign key bind
        return profileRepository.save(profile);
    }

    // ===============================
    // UPDATE PROFILE (ALL FIELDS)
    // ===============================
    public AlumniProfile updateProfile(User user, AlumniProfile updatedProfile) {

        AlumniProfile existingProfile = profileRepository
                .findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Alumni profile not found"));

        // 🔥 ACADEMIC
        existingProfile.setName(updatedProfile.getName());
        existingProfile.setPrn(updatedProfile.getPrn());
        existingProfile.setBtechBranch(updatedProfile.getBtechBranch());
        existingProfile.setPassingYear(updatedProfile.getPassingYear());
        existingProfile.setCgpa(updatedProfile.getCgpa());

        // 🔥 PROFESSIONAL
        existingProfile.setIndustry(updatedProfile.getIndustry());
        existingProfile.setExperienceYears(updatedProfile.getExperienceYears());
        existingProfile.setCurrentCompany(updatedProfile.getCurrentCompany());
        existingProfile.setPhoneNumber(updatedProfile.getPhoneNumber());

        // 🔥 SKILLS & ACHIEVEMENTS
        existingProfile.setSkills(updatedProfile.getSkills());
        existingProfile.setAchievements(updatedProfile.getAchievements());

        // 🔐 SAFETY: ensure FK never breaks
        existingProfile.setUser(user);

        return profileRepository.save(existingProfile);
    }

    // ===============================
    // FETCH PROFILE
    // ===============================
    public AlumniProfile getProfileByUserId(Long userId) {
        return profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found"));
    }

    // ===============================
    // EXISTS CHECK
    // ===============================
    public boolean profileExists(Long userId) {
        return profileRepository.findByUser_Id(userId).isPresent();
    }

    public List<AlumniProfile> getAllAlumniProfiles() {
        return profileRepository.findAll();
    }

    public Page<AlumniDirectoryDto> getDirectory(Pageable pageable) {
        return profileRepository.fetchDirectory(pageable);
    }
}
