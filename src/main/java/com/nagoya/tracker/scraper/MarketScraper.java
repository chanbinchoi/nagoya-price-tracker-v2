package com.nagoya.tracker.scraper;

import com.nagoya.tracker.repository.ProductRepository;

public interface MarketScraper {
    void scrape(ProductRepository productRepository);
}
