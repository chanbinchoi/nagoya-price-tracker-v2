package com.nagoya.tracker.scraper;

import com.nagoya.tracker.domain.PriceHistory;
import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.repository.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AmicaScraper implements MarketScraper {

    private static final String URL = "https://www.amicashop.com/products/list?name=%E3%83%90%E3%83%8A%E3%83%8A";

    @Override
    public void scrape(ProductRepository productRepository) {
        System.out.println("[AmicaScraper] Started scraping...");

        Document doc;
        try {
            doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .timeout(10_000)
                    .get();
        } catch (IOException e) {
            System.out.println("[AmicaScraper] ERROR: Failed to fetch page - " + e.getMessage());
            return;
        }

        Elements items = doc.select("ul.product_list li");
        int savedCount = 0;
        int skippedCount = 0;

        for (Element item : items) {
            String itemName = item.select("span.product_name").text().trim();
            String rawPrice = item.select("span.nomal_price").text();

            if (itemName.isEmpty() || rawPrice.isEmpty()) continue;

            String digits = rawPrice.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) continue;

            int crawledPrice;
            try {
                crawledPrice = Integer.parseInt(digits);
            } catch (NumberFormatException e) {
                continue;
            }

            Product product = productRepository.findByStoreNameAndItemName("Amica", itemName)
                    .orElse(new Product("Amica", itemName));

            int latestPrice = product.getLatestPrice();

            if (latestPrice == 0 || latestPrice != crawledPrice) {
                product.getPriceHistories().add(new PriceHistory(product, crawledPrice, LocalDateTime.now()));
                productRepository.save(product);
                String reason = latestPrice == 0 ? "NEW" : "PRICE_CHANGED(" + latestPrice + " -> " + crawledPrice + ")";
                System.out.println("[AmicaScraper] SAVED: " + itemName + " (" + reason + ")");
                savedCount++;
            } else {
                System.out.println("[AmicaScraper] SKIPPED: " + itemName + " (no change, " + latestPrice + " JPY)");
                skippedCount++;
            }
        }

        System.out.println("[AmicaScraper] Completed - total: " + items.size() + " / saved: " + savedCount + " / skipped: " + skippedCount);
    }
}
