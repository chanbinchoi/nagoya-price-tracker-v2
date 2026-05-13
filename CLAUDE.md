# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Test
./mvnw test

# Run single test class
./mvnw test -Dtest=NagoyaPriceTrackerV2ApplicationTests
```

## Architecture

Spring Boot 4.0 / Java 17 / H2 in-memory DB / Lombok / Jsoup

### Data flow

```
CrawlerService (@Scheduled, every 10s)
  → deletes all DB rows, re-crawls mock HTML via Jsoup
  → saves Product list via ProductRepository (JPA)

GET /api/products/lowest-price
  → ProductController
      → ProductService.getLowestPriceProduct()  (in-memory stream min)
      → ExchangeRateService.getJpyToKrwRate()   (RestTemplate → exchangerate-api.com)
  → returns ProductResponse { success, storeName, priceJpy, priceKrw, exchangeRate, message }

Frontend (src/main/resources/static/index.html)
  → vanilla JS fetch → /api/products/lowest-price → DOM binding
```

### Key design notes

- **CrawlerService** currently uses hardcoded mock HTML instead of a real HTTP request (`Jsoup.connect(...).get()`). The mock simulates three Nagoya supermarkets (V-drug, Yamanaka, Amica) selling bananas at different JPY prices.
- **H2 in-memory DB** — data is wiped on every restart and also on every 10-second crawl cycle (`productRepository.deleteAll()` before each re-crawl).
- **ExchangeRateService** falls back to a hardcoded rate of `9.0` (KRW per JPY) if the external API call fails.
- `ProductRepository` is a bare `JpaRepository<Product, Long>` with no custom queries — all filtering is done in Java streams inside `ProductService`.
- Lombok is used throughout: `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor`. Do not add setters to `Product` — it is treated as immutable after construction.
