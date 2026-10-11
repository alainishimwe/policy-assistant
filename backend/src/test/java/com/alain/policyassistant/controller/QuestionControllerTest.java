package com.alain.policyassistant.controller;

import com.alain.policyassistant.exception.AiUnavailableException;
import com.alain.policyassistant.exception.GlobalExceptionHandler;
import com.alain.policyassistant.service.PolicyService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
@Import(GlobalExceptionHandler.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PolicyService policyService;

    @Test
    void returnsAnswerWhenServiceSucceeds() throws Exception {
        when(policyService.answerQuestion("What is my deductible?"))
                .thenReturn("The collision deductible is $500.");

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "question": "What is my deductible?"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer")
                        .value("The collision deductible is $500."));
    }

    @Test
    void returns503WhenAiIsUnavailable() throws Exception {
        when(policyService.answerQuestion("What is my deductible?"))
                .thenThrow(new AiUnavailableException(
                        new RuntimeException("Simulated connection failure")
                ));

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "question": "What is my deductible?"
                                }
                                """))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message")
                        .value(
                            "We couldn't generate an answer right now. "
                            + "Please try again later."
                        ));
    }
}