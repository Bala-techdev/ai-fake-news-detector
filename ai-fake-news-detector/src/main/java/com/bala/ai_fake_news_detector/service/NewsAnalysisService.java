package com.bala.ai_fake_news_detector.service;

import com.bala.ai_fake_news_detector.dto.NewsAnalysisResponse;
import com.bala.ai_fake_news_detector.entity.NewsAnalysis;
import com.bala.ai_fake_news_detector.exception.ResourceNotFoundException;
import com.bala.ai_fake_news_detector.repository.NewsAnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsAnalysisService {

    private final NewsAnalysisRepository newsAnalysisRepository;
    private final AiAnalysisClient aiAnalysisClient;

    public NewsAnalysisService(NewsAnalysisRepository newsAnalysisRepository, AiAnalysisClient aiAnalysisClient) {
        this.newsAnalysisRepository = newsAnalysisRepository;
        this.aiAnalysisClient = aiAnalysisClient;
    }

    @Transactional
    public NewsAnalysisResponse analyzeAndSave(String content) {
        AiAnalysisResult aiAnalysisResult = aiAnalysisClient.analyze(content);

        NewsAnalysis entity = new NewsAnalysis();
        entity.setContent(content);
        entity.setCredibilityScore(aiAnalysisResult.credibilityScore());
        entity.setExplanation(aiAnalysisResult.explanation());
        entity.setVerdict(aiAnalysisResult.verdict());

        NewsAnalysis saved = newsAnalysisRepository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<NewsAnalysisResponse> getHistory() {
        return newsAnalysisRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NewsAnalysisResponse getById(Long id) {
        NewsAnalysis item = newsAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found for id: " + id));

        return toResponse(item);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!newsAnalysisRepository.existsById(id)) {
            throw new ResourceNotFoundException("Analysis not found for id: " + id);
        }
        newsAnalysisRepository.deleteById(id);
    }

    private NewsAnalysisResponse toResponse(NewsAnalysis item) {
        return new NewsAnalysisResponse(
                item.getId(),
                item.getContent(),
                item.getCredibilityScore(),
                item.getExplanation(),
                item.getVerdict(),
                item.getCreatedAt()
        );
    }
}
