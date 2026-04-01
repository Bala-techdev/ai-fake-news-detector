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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GeminiAnalysisClient implements AiAnalysisClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiUrl;
    private final String apiKey;

    public GeminiAnalysisClient(
            @Value("${app.ai.api-url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}") String apiUrl,
            @Value("${app.ai.api-key:}") String apiKey
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Override
    public AiAnalysisResult analyze(String content) {
        validateConfiguration();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", buildPrompt(content))
                                )
                        )
                )
        );

        String requestUrl = apiUrl + "?key=" + apiKey;

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                    requestUrl,
                    new HttpEntity<>(body, headers),
                    JsonNode.class
            );

            JsonNode root = response.getBody();
            if (root == null) {
                throw new ExternalServiceException("Gemini API returned an empty response");
            }

            String aiContent = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            if (!StringUtils.hasText(aiContent)) {
                throw new ExternalServiceException("Gemini API response did not contain analysis content");
            }

            return parseAiResponse(aiContent);
        } catch (HttpStatusCodeException ex) {
            String message = "Gemini API call failed with status "
                    + ex.getStatusCode().value() + " " + ex.getStatusText();

            String responseBody = ex.getResponseBodyAsString();
            if (StringUtils.hasText(responseBody)) {
                message += ": " + truncate(responseBody, 400);
            }

            throw new ExternalServiceException(message, ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Failed to call Gemini API: " + ex.getMessage(), ex);
        }
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(apiKey)) {
            throw new ExternalServiceException("Gemini API key is not configured. Set GEMINI_API_KEY in the environment or in a local .env file.");
        }
    }

    private AiAnalysisResult parseAiResponse(String aiContent) {
        try {
            String cleaned = extractJsonPayload(stripCodeFence(aiContent));
            JsonNode parsed = objectMapper.readTree(cleaned);

            double rawScore = parsed.path("credibilityScore").asDouble(Double.NaN);
            String explanation = parsed.path("explanation").asText();
            String verdict = parsed.path("verdict").asText();

            if (Double.isNaN(rawScore) || !StringUtils.hasText(explanation) || !StringUtils.hasText(verdict)) {
                throw new ExternalServiceException("Gemini API returned invalid analysis format");
            }

            double normalizedScore = Math.max(0.0, Math.min(100.0, rawScore));
            String normalizedVerdict = normalizeVerdict(verdict);

            return new AiAnalysisResult(normalizedScore, explanation.trim(), normalizedVerdict);
        } catch (Exception ex) {
            if (ex instanceof ExternalServiceException externalServiceException) {
                throw externalServiceException;
            }
            throw new ExternalServiceException("Failed to parse Gemini analysis response", ex);
        }
    }

    private String normalizeVerdict(String verdict) {
        String normalized = verdict.trim().toLowerCase();
        return switch (normalized) {
            case "real" -> "Real";
            case "fake" -> "Fake";
            default -> "Suspicious";
        };
    }

    private String stripCodeFence(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            String withoutStart = trimmed.replaceFirst("^```[a-zA-Z]*", "").trim();
            return withoutStart.substring(0, withoutStart.length() - 3).trim();
        }
        return trimmed;
    }

    private String extractJsonPayload(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }

    private String buildPrompt(String content) {
        return "Check whether this news text is real, fake, or suspicious based on general knowledge, logical consistency, and common misinformation patterns. "
                + "Return strictly valid JSON with exactly these fields: "
                + "{\"credibilityScore\": number between 0 and 100, \"explanation\": \"short explanation\", \"verdict\": \"Real\" | \"Fake\" | \"Suspicious\"}. "
                + "News text: " + content;
    }

    private String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }
}