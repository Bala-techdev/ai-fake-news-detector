package com.bala.ai_fake_news_detector.controller;

import com.bala.ai_fake_news_detector.dto.AnalyzeRequest;
import com.bala.ai_fake_news_detector.dto.NewsAnalysisResponse;
import com.bala.ai_fake_news_detector.service.NewsAnalysisService;
// import com.bala.ai_fake_news_detector.service.AiAnalysisClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NewsAnalysisControllerTest {

    @Test
    void analyzeReturnsCreatedResponse() throws Exception {
  NewsAnalysisService newsAnalysisService = mock(NewsAnalysisService.class);
  NewsAnalysisController controller = new NewsAnalysisController(newsAnalysisService);
  NewsAnalysisResponse expected = new NewsAnalysisResponse(
    1L,
    "NASA announced a new satellite mission.",
    82.5,
    "Claim looks mostly credible.",
    "Real",
    null
  );

  given(newsAnalysisService.analyzeAndSave("NASA announced a new satellite mission."))
    .willReturn(expected);

  ResponseEntity<NewsAnalysisResponse> response = controller.analyze(
    new AnalyzeRequest("NASA announced a new satellite mission.")
  );

  assertEquals(HttpStatus.CREATED, response.getStatusCode());
  assertNotNull(response.getBody());
  assertEquals("NASA announced a new satellite mission.", response.getBody().content());
  assertEquals(82.5, response.getBody().credibilityScore());
  assertEquals("Claim looks mostly credible.", response.getBody().explanation());
  assertEquals("Real", response.getBody().verdict());
    }
}