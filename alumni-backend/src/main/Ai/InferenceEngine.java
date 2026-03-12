package com.alumni.alumni_backend.ai;

import org.springframework.stereotype.Component;
import java.util.*;
import com.alumni.alumni_backend.model.AlumniProfile;
import com.alumni.alumni_backend.repository.AlumniProfileRepository;

@Component
public class InferenceEngine {

    private final AlumniProfileRepository profileRepository;

    public InferenceEngine(AlumniProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public String processQuery(String query) {

        List<AlumniProfile> profiles = profileRepository.findAll();

        Map<String, Integer> branchScore = new HashMap<>();

        boolean likesCoding = query.toLowerCase().contains("coding")
                || query.toLowerCase().contains("software")
                || query.toLowerCase().contains("programming");

        for (AlumniProfile p : profiles) {

            String branch = p.getBtechBranch();
            String skills = Optional.ofNullable(p.getSkills()).orElse("").toLowerCase();
            String company = Optional.ofNullable(p.getCurrentCompany()).orElse("").toLowerCase();

            int score = 0;

            if (likesCoding && (skills.contains("java") || skills.contains("python") || skills.contains("backend"))) {
                score += 2;
            }

            if (company.contains("tech") || company.contains("software") || company.contains("solutions")) {
                score += 1;
            }

            branchScore.put(branch, branchScore.getOrDefault(branch, 0) + score);
        }

        String bestBranch = branchScore.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Computer Engineering");

        return "Based on alumni outcomes and your interest in coding, the most recommended branch is: "
                + bestBranch + ". This branch shows strong alignment with software roles and industry demand.";
    }
}