package com.alumni.alumni_backend.ai;

import org.springframework.web.bind.annotation.*;
import com.alumni.alumni_backend.ai.dto.AiRequestDto;
import com.alumni.alumni_backend.ai.dto.AiResponseDto;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/query")
    public AiResponseDto query(@RequestBody AiRequestDto request) {
        return aiService.getAdvice(request.getQuery());
    }
}