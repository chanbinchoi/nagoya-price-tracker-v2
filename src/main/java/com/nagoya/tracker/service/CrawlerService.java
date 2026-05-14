package com.nagoya.tracker.service;

import com.nagoya.tracker.repository.ProductRepository;
import com.nagoya.tracker.scraper.MarketScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final ProductRepository productRepository;
    private final List<MarketScraper> scrapers;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void scrapeAndSaveData() {
        scrapers.forEach(scraper -> scraper.scrape(productRepository));
    }
}
