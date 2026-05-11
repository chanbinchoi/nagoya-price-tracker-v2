package com.nagoya.tracker.service;

import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final ProductRepository productRepository;

    // Execute Crawling, Save DB
    public void scrapeAndSaveData() {
        // In reality, it is retrieved using Jsoup.connect("http://...").get(), but Mock HTML is used for training.
        String mockHtml = """
                <div id='supermarket-list'>
                    <div class='item'><span class='store'>V-drug</span><span class='name'>banana</span><span class='price'>140</span></div>
                    <div class='item'><span class='store'>Yamanaka</span><span class='name'>banana</span><span class='price'>170</span></div>
                    <div class='item'><span class='store'>Amica</span><span class='name'>banana</span><span class='price'>120</span></div>
                </div>
                """;

        List<Product> crawledProducts = new ArrayList<>();

        // 1. HTML -> Jsoup Document
        Document doc = Jsoup.parse(mockHtml);

        // 2. Extract All Data - CSS Selector
        Elements items = doc.select("div.item");

        // 3. Extract Data - Loop
        for (Element item : items) {
            String storeName = item.select("span.store").text();
            String itemName = item.select("span.name").text();
            int price = Integer.parseInt(item.select("span.price").text());

            crawledProducts.add(new Product(storeName, itemName, price));
        }

        // 4. Save All Data to DB (Spring Data JPA)
        productRepository.saveAll(crawledProducts);
        System.out.println("Crawled data loaded into DB completed: " + crawledProducts.size() + "cases");


    }
}
