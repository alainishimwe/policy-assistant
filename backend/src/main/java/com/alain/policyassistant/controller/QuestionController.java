package com.alain.policyassistant.controller;

import jakarta.validation.Valid;
import com.alain.policyassistant.dto.QuestionRequest;
import org.springframework.web.bind.annotation.*;
import com.alain.policyassistant.service.PolicyService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final PolicyService policyService;

    public QuestionController(PolicyService policyService) {
        this.policyService = policyService;
    }

    public record QuestionResponse(String answer) {}

    @PostMapping
    public QuestionResponse askQuestion(
        @Valid @RequestBody QuestionRequest request
    ) {
        return new QuestionResponse(
            policyService.answerQuestion(request.question())
        );
    }
}