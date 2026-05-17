package com.nagoya.tracker.scraper;

import com.nagoya.tracker.domain.PriceHistory;
import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.repository.ProductRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AeonScraper implements MarketScraper {

    private static final String URL =
            "https://shop.aeon.com/netsuper/catalogsearch/result/?q=%E3%83%90%E3%83%8A%E3%83%8A";

    @Override
    public void scrape(ProductRepository productRepository) {
        System.out.println("[AeonScraper] Started scraping...");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(URL);

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.product-item")));

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements items = doc.select("li.product-item");
            int savedCount = 0;
            int skippedCount = 0;

            for (Element item : items) {
                String itemName = item.select("a.product-item-link").text().trim();
                String rawPrice = item.select(".price-box .price").first() != null
                        ? item.select(".price-box .price").first().text()
                        : "";

                if (itemName.isEmpty() || rawPrice.isEmpty()) continue;

                String digits = rawPrice.replaceAll("[^0-9]", "");
                if (digits.isEmpty()) continue;

                int crawledPrice;
                try {
                    crawledPrice = Integer.parseInt(digits);
                } catch (NumberFormatException e) {
                    continue;
                }

                Product product = productRepository.findByStoreNameAndItemName("Aeon", itemName)
                        .orElse(new Product("Aeon", itemName));

                int latestPrice = product.getLatestPrice();

                if (latestPrice == 0 || latestPrice != crawledPrice) {
                    product.getPriceHistories().add(new PriceHistory(product, crawledPrice, LocalDateTime.now()));
                    productRepository.save(product);
                    String reason = latestPrice == 0 ? "NEW" : "PRICE_CHANGED(" + latestPrice + " -> " + crawledPrice + ")";
                    System.out.println("[AeonScraper] SAVED: " + itemName + " (" + reason + ")");
                    savedCount++;
                } else {
                    System.out.println("[AeonScraper] SKIPPED: " + itemName + " (no change, " + latestPrice + " JPY)");
                    skippedCount++;
                }
            }

            System.out.println("[AeonScraper] Completed - total: " + items.size() + " / saved: " + savedCount + " / skipped: " + skippedCount);

        } catch (Exception e) {
            System.out.println("[AeonScraper] ERROR: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
