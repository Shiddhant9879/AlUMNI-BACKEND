package com.alumni.alumni_backend.ai;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final InferenceEngine inferenceEngine;

    public AiService(InferenceEngine inferenceEngine) {
        this.inferenceEngine = inferenceEngine;
    }

    public String getAdvice(String query) {
        return inferenceEngine.processQuery(query);
    }
}