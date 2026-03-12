package com.alumni.alumni_backend.repository;

import com.alumni.alumni_backend.dto.AlumniDirectoryDto;
import com.alumni.alumni_backend.model.AlumniProfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional; // ✅ REQUIRED

public interface AlumniProfileRepository
        extends JpaRepository<AlumniProfile, Long> {

    // 🔍 Directory fetch (paginated, read-only)
    @Query("""
                SELECT new com.alumni.alumni_backend.dto.AlumniDirectoryDto(
                    ap.name,
                    ap.prn,
                    ap.btechBranch,
                    ap.passingYear,
                    ap.currentCompany,
                    ap.skills,
                    ap.achievements
                )
                FROM AlumniProfile ap
            """)
    Page<AlumniDirectoryDto> fetchDirectory(Pageable pageable);

    // 🔐 Profile existence & fetch by logged-in user
    Optional<AlumniProfile> findByUser_Id(Long userId);
}
