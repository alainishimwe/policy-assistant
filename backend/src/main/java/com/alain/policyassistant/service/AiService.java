package com.alain.policyassistant.service;

import java.time.Duration;
import com.alain.policyassistant.exception.AiUnavailableException;
import com.openai.errors.OpenAIException;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

import org.springframework.stereotype.Service;

import com.openai.errors.OpenAIServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.openai.models.embeddings.EmbeddingCreateParams;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);
    private final OpenAIClient client;

    public AiService() {
        client = OpenAIOkHttpClient.builder()
                .fromEnv()
                .timeout(Duration.ofSeconds(20))
                .maxRetries(0)
                .build();
    }

    public String generateAnswer(String prompt) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-4.1-mini")
                .input(prompt)
                .maxOutputTokens(200L)
                .store(false)
                .build();

    
        Response response;

        try {
            response = client.responses().create(params);
        } catch (OpenAIServiceException exception) {
            log.warn("OpenAI request failed with HTTP {} ({})",
                exception.statusCode(), exception.getClass().getSimpleName());
            throw new AiUnavailableException(exception);
        } catch (OpenAIException exception) {
            log.warn("OpenAI request failed ({})",
                exception.getClass().getSimpleName());
            throw new AiUnavailableException(exception);
        }

        StringBuilder answer = new StringBuilder();

        response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .forEach(text -> answer.append(text.text()));

        return answer.toString();
    }

    public List<Float> embed(String text) {
        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .model("text-embedding-3-small")
                .input(text)
                .build();

        try {
            return client.embeddings()
                    .create(params)
                    .data()
                    .getFirst()
                    .embedding();
        } catch (OpenAIServiceException exception) {
            log.warn("OpenAI embedding request failed with HTTP {} ({})",
                    exception.statusCode(), exception.getClass().getSimpleName());
            throw new AiUnavailableException(exception);
        } catch (OpenAIException exception) {
            log.warn("OpenAI embedding request failed ({})",
                    exception.getClass().getSimpleName());
            throw new AiUnavailableException(exception);
        }
    }
}