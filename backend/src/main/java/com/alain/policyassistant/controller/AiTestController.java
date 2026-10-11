package com.alain.policyassistant.controller;

import com.alain.policyassistant.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-test")
public class AiTestController {

    private final AiService aiService;

    public AiTestController(AiService aiService) {
        this.aiService = aiService;
    }

    public record AiTestResponse(String answer) {}

    @PostMapping
    public AiTestResponse testConnection() {
        String answer = aiService.generateAnswer(
                "Explain an insurance deductible in one short sentence."
        );

        return new AiTestResponse(answer);
    }
}