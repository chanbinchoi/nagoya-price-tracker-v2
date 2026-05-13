package com.nagoya.tracker.service;

import com.nagoya.tracker.domain.PriceHistory;
import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 10000)
    public void scrapeAndSaveData() {
        System.out.println("Start of periodic data crawling bot operation...");

        String mockHtml = """
                <div id='supermarket-list'>
                    <div class='item'><span class='store'>V-drug</span><span class='name'>banana</span><span class='price'>140</span></div>
                    <div class='item'><span class='store'>Yamanaka</span><span class='name'>banana</span><span class='price'>170</span></div>
                    <div class='item'><span class='store'>Amica</span><span class='name'>banana</span><span class='price'>120</span></div>
                </div>
                """;

        Document doc = Jsoup.parse(mockHtml);
        Elements items = doc.select("div.item");

        for (Element item : items) {
            String storeName = item.select("span.store").text();
            String itemName = item.select("span.name").text();
            int price = Integer.parseInt(item.select("span.price").text());

            Product product = productRepository.findByStoreNameAndItemName(storeName, itemName)
                    .orElse(new Product(storeName, itemName));

            product.getPriceHistories().add(new PriceHistory(product, price, LocalDateTime.now()));
            productRepository.save(product);
        }

        System.out.println("Crawling completed: " + items.size() + " items recorded.");
    }
}
