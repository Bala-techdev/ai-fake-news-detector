package com.bala.ai_fake_news_detector.repository;

import com.bala.ai_fake_news_detector.entity.NewsAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsAnalysisRepository extends JpaRepository<NewsAnalysis, Long> {

    List<NewsAnalysis> findAllByOrderByCreatedAtDesc();
}
