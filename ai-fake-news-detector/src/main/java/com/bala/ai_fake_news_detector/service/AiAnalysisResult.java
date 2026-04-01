package com.bala.ai_fake_news_detector.service;

public record AiAnalysisResult(
        Double credibilityScore,
        String explanation,
        String verdict
) {
}
