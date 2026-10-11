package com.alain.policyassistant.controller;

import org.springframework.web.bind.annotation.*;
import com.alain.policyassistant.service.PolicyService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final PolicyService policyService;

    public QuestionController(PolicyService policyService) {
        this.policyService = policyService;
    }
    public record QuestionRequest(String question) {}

    public record QuestionResponse(String answer) {}

    @PostMapping
    public QuestionResponse askQuestion(
        @RequestBody QuestionRequest request
    ) {
        return new QuestionResponse(
            policyService.answerQuestion(request.question())
        );
    }
}