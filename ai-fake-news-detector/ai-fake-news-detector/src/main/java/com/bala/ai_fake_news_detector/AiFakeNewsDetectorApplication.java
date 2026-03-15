package com.bala.ai_fake_news_detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiFakeNewsDetectorApplication {

	public static void main(String[] args) {
		DotenvPropertyLoader.load();
		SpringApplication.run(AiFakeNewsDetectorApplication.class, args);
	}

}
