package com.bala.ai_fake_news_detector.dto;

import java.time.Instant;

public record NewsAnalysisResponse(
        Long id,
        String content,
        Double credibilityScore,
        String explanation,
        Instant createdAt
) {
}
