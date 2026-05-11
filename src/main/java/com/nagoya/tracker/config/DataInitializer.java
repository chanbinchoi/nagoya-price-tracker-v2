package com.nagoya.tracker.config;

import com.nagoya.tracker.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Spring Bean
@RequiredArgsConstructor // Auto Constructor (Lombok)
public class DataInitializer implements CommandLineRunner {

    private final CrawlerService crawlerService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Data crawling bot launched...");
        crawlerService.scrapeAndSaveData();
    }
}
