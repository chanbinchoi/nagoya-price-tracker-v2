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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AinomaScraper implements MarketScraper {

    private static final String AINOMA_ID = "chanhw96@naver.com";
    private static final String AINOMA_PW = "project0518";

    @Override
    public void scrape(ProductRepository productRepository) {
        System.out.println("[AinomaScraper] Started E2E Login Automation...");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.get("https://www.ainomarket.com/netshop-web/login/auth/");

            WebElement idInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-id")));
            WebElement pwInput = driver.findElement(By.id("input-password"));
            WebElement loginBtn = driver.findElement(By.cssSelector(".submitbtn"));

            System.out.println("[AinomaScraper] Injecting Credentials...");
            idInput.sendKeys(AINOMA_ID);
            pwInput.sendKeys(AINOMA_PW);
            loginBtn.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
            System.out.println("[AinomaScraper] Login Success! Session Acquired.");

            driver.get("https://www.ainomarket.com/netshop-web/item?keyword=%E3%83%90%E3%83%8A%E3%83%8A&page=1&sortBy=1");
            Thread.sleep(5000);

            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);

            // 진짜 CSS 셀렉터 적용
            Elements items = doc.select(".itemList-sale_block");

            int saved = 0, skipped = 0;
            for (Element item : items) {
                String itemName = item.select(".itemList-sale_name a").text();
                // ownText()를 사용하여 하위 태그 텍스트 제외 후 숫자만 파싱
                String priceText = item.select(".recommend_price > span").first().ownText().replaceAll("[^0-9]", "");
                if (priceText.isEmpty()) continue;

                int price = Integer.parseInt(priceText);

                Product product = productRepository.findByStoreNameAndItemName("Valor", itemName)
                        .orElse(new Product("Valor", itemName));

                int latestPrice = product.getLatestPrice();
                if (latestPrice == 0 || latestPrice != price) {
                    product.getPriceHistories().add(new PriceHistory(product, price, LocalDateTime.now()));
                    productRepository.save(product);
                    saved++;
                    System.out.println("[AinomaScraper] SAVED: " + itemName + " ( " + price + "円 )");
                } else {
                    skipped++;
                }
            }
            System.out.println("[AinomaScraper] Completed - saved: " + saved + " / skipped: " + skipped);

        } catch (Exception e) {
            System.out.println("[AinomaScraper] ERROR: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}