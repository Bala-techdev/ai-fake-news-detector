package com.bala.ai_fake_news_detector.service;

import com.bala.ai_fake_news_detector.exception.ExternalServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiAnalysisClient implements AiAnalysisClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiUrl;
    private final String apiKey;
    private final String model;

    public OpenAiAnalysisClient(
            @Value("${app.ai.api-url:https://api.openai.com/v1/chat/completions}") String apiUrl,
            @Value("${app.ai.api-key:}") String apiKey,
            @Value("${app.ai.model:gpt-4o-mini}") String model
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public AiAnalysisResult analyze(String content) {
        validateConfiguration();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "temperature", 0.1,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt()),
                        Map.of("role", "user", "content", userPrompt(content))
                )
        );

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                    apiUrl,
                    new HttpEntity<>(body, headers),
                    JsonNode.class
            );

            JsonNode root = response.getBody();
            if (root == null) {
                throw new ExternalServiceException("AI API returned an empty response");
            }

            String aiContent = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText();

            if (!StringUtils.hasText(aiContent)) {
                throw new ExternalServiceException("AI API response did not contain analysis content");
            }

            return parseAiResponse(aiContent);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Failed to call AI API", ex);
        }
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(apiKey)) {
            throw new ExternalServiceException("AI API key is not configured. Set app.ai.api-key in application.properties.");
        }
    }

    private AiAnalysisResult parseAiResponse(String aiContent) {
        try {
            String cleaned = stripCodeFence(aiContent);
            JsonNode parsed = objectMapper.readTree(cleaned);

            double rawScore = parsed.path("credibilityScore").asDouble(Double.NaN);
            String explanation = parsed.path("explanation").asText();

            if (Double.isNaN(rawScore) || !StringUtils.hasText(explanation)) {
                throw new ExternalServiceException("AI API returned invalid analysis format");
            }

            double normalizedScore = Math.max(0.0, Math.min(100.0, rawScore));
            return new AiAnalysisResult(normalizedScore, explanation.trim());
        } catch (Exception ex) {
            if (ex instanceof ExternalServiceException externalServiceException) {
                throw externalServiceException;
            }
            throw new ExternalServiceException("Failed to parse AI analysis response", ex);
        }
    }

    private String stripCodeFence(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            String withoutStart = trimmed.replaceFirst("^```[a-zA-Z]*", "").trim();
            return withoutStart.substring(0, withoutStart.length() - 3).trim();
        }
        return trimmed;
    }

    private String systemPrompt() {
        return "You are a fact-checking assistant. Analyze whether a news statement appears credible based on general knowledge, logical consistency, and common misinformation patterns.";
    }

    private String userPrompt(String content) {
        return "Analyze this news text and return strictly valid JSON with exactly these fields: "
                + "{\"credibilityScore\": number between 0 and 100, \"explanation\": \"short explanation\"}. "
                + "News text: " + content;
    }
}
