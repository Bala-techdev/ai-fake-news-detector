package com.bala.ai_fake_news_detector.controller;

import com.bala.ai_fake_news_detector.dto.AnalyzeRequest;
import com.bala.ai_fake_news_detector.dto.NewsAnalysisResponse;
import com.bala.ai_fake_news_detector.service.NewsAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsAnalysisController {

    private final NewsAnalysisService newsAnalysisService;

    public NewsAnalysisController(NewsAnalysisService newsAnalysisService) {
        this.newsAnalysisService = newsAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<NewsAnalysisResponse> analyze(@Valid @RequestBody AnalyzeRequest request) {
        NewsAnalysisResponse response = newsAnalysisService.analyzeAndSave(request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<NewsAnalysisResponse>> history() {
        return ResponseEntity.ok(newsAnalysisService.getHistory());
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<NewsAnalysisResponse> historyById(@PathVariable Long id) {
        return ResponseEntity.ok(newsAnalysisService.getById(id));
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        newsAnalysisService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
