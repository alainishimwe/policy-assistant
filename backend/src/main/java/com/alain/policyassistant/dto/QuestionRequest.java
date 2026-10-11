package com.alain.policyassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionRequest(
        @NotBlank(message = "Please enter a question.")
        @Size(
                max = 1000,
                message = "Please keep your question to 1,000 characters or fewer."
        )
        String question
) {}