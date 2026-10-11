package com.alain.policyassistant.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final OpenAIClient client;

    public AiService() {
        client = OpenAIOkHttpClient.fromEnv();
    }

    public String generateAnswer(String prompt) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-4.1-mini")
                .input(prompt)
                .maxOutputTokens(200L)
                .store(false)
                .build();

        Response response = client.responses().create(params);

        StringBuilder answer = new StringBuilder();

        response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .forEach(text -> answer.append(text.text()));

        return answer.toString();
    }
}