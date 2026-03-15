package com.bala.ai_fake_news_detector.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeRequest(
        @NotBlank(message = "content is required")
        String content
) {
}
