package com.alumni.alumni_backend.controller;

import com.alumni.alumni_backend.dto.AlumniDirectoryDto;
import com.alumni.alumni_backend.repository.AlumniProfileRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/directory")
@CrossOrigin
public class DirectoryController {

    private final AlumniProfileRepository repository;

    public DirectoryController(AlumniProfileRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Page<AlumniDirectoryDto> getDirectory(
            @RequestParam(defaultValue = "0") int page) {

        return repository.fetchDirectory(PageRequest.of(page, 10));
    }
}
