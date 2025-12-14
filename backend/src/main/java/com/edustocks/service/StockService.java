package com.edustocks.service;

import com.edustocks.model.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    @Value("${stock.api.key}")
    private String apiKey;

    @Value("${stock.api.base-url}")
    private String baseUrl;

    private final WebClient webClient = WebClient.create();

    // Simple popular list; you can adjust this list as needed
    private final List<String> popularStocks = List.of(
            "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA",
            "META", "NVDA", "JPM", "V", "JNJ"
    );

    // In-memory cache to avoid hitting Alpha Vantage rate limits too often
    private static class CachedStock {
        final Stock stock;
        final Instant timestamp;

        CachedStock(Stock stock, Instant timestamp) {
            this.stock = stock;
            this.timestamp = timestamp;
        }
    }

    private final Map<String, CachedStock> cache = new ConcurrentHashMap<>();
    // cache TTL in seconds
    private final long cacheTtlSeconds = 60;

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        for (String symbol : popularStocks) {
            try {
                stocks.add(getStockBySymbol(symbol));
            } catch (Exception e) {
                // Log the error but continue with next stock
                System.err.println("Warning: Failed to fetch stock " + symbol + ": " + e.getMessage());
                // Don't add stub data - let caller handle empty results
            }
        }
        return stocks;
    }

    public Stock getStockBySymbol(String symbol) {
        // Check cache first
        CachedStock cached = cache.get(symbol);
        if (cached != null && Instant.now().minusSeconds(cacheTtlSeconds).isBefore(cached.timestamp)) {
            return cached.stock;
        }

        // API key is required for real stock data
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Stock API key not configured. Set 'stock.api.key' property.");
        }

        try {
            // Use Alpha Vantage GLOBAL_QUOTE endpoint for latest price
            String uri = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", baseUrl, symbol, apiKey);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.isEmpty() || response.get("Global Quote") == null) {
                throw new RuntimeException("Invalid response from Alpha Vantage API for symbol: " + symbol);
            }

            @SuppressWarnings("unchecked")
            Map<String, String> quote = (Map<String, String>) response.get("Global Quote");

            String priceStr = quote.getOrDefault("05. price", "0");
            if (priceStr.equals("0")) {
                throw new RuntimeException("No price data available for symbol: " + symbol);
            }

            String changeStr = quote.getOrDefault("09. change", "0");
            String changePercentStr = quote.getOrDefault("10. change percent", "0%");
            String volumeStr = quote.getOrDefault("06. volume", "0");

            double price = parseDoubleSafe(priceStr);
            double change = parseDoubleSafe(changeStr);
            double changePercent = parsePercentSafe(changePercentStr);
            long volume = parseLongSafe(volumeStr);

            Stock stock = new Stock(symbol, symbol, price, change, changePercent, volume, "NYSE");
            cache.put(symbol, new CachedStock(stock, Instant.now()));
            return stock;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock data for symbol: " + symbol + ". Error: " + e.getMessage(), e);
        }
    }

    public List<Stock> searchStocks(String query) {
        List<Stock> allStocks = getAllStocks();
        return allStocks.stream()
                .filter(stock -> stock.getSymbol().toLowerCase().contains(query.toLowerCase()) ||
                        stock.getName().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    private double parseDoubleSafe(String v) {
        try {
            return Double.parseDouble(v);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private long parseLongSafe(String v) {
        try {
            return Long.parseLong(v);
        } catch (Exception e) {
            return 0L;
        }
    }

    private double parsePercentSafe(String v) {
        try {
            return Double.parseDouble(v.replace("%", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }


}



