package com.edustocks.service;

import com.edustocks.model.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    @Value("${stock.api.key:}")
    private String apiKey;

    @Value("${stock.api.base-url:https://www.alphavantage.co/query}")
    private String baseUrl;

    private final WebClient webClient = WebClient.create();

    // Simple popular list; you can adjust this list as needed
    private final List<String> popularStocks = List.of(
            "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA",
            "META", "NVDA", "JPM", "V", "JNJ",
            "WMT", "PG", "KO", "MCD", "DIS",
            "BA", "GE", "F", "GM", "CSCO",
            "ORCL", "SAP", "IBM", "INTC", "AMD"
    );

    // Sample stock data as fallback when API fails or for demo purposes
    private final Map<String, Stock> sampleStocks = new ConcurrentHashMap<>();

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
    // cache TTL - 10 minutes to avoid rate limits (free tier: 5 calls/min)
    private final long cacheTtlSeconds = 600;

    @PostConstruct
    public void initSampleData() {
        // Initialize with realistic sample data as fallback
        sampleStocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 264.58, 4.00, 1.54, 42070499L, "NASDAQ"));
        sampleStocks.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 411.25, 2.15, 0.53, 18234567L, "NASDAQ"));
        sampleStocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 178.92, -1.23, -0.68, 21456789L, "NASDAQ"));
        sampleStocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 225.47, 3.21, 1.44, 35678901L, "NASDAQ"));
        sampleStocks.put("TSLA", new Stock("TSLA", "Tesla Inc.", 285.75, -5.42, -1.86, 89012345L, "NASDAQ"));
        sampleStocks.put("META", new Stock("META", "Meta Platforms Inc.", 630.89, 8.54, 1.37, 12345678L, "NASDAQ"));
        sampleStocks.put("NVDA", new Stock("NVDA", "NVIDIA Corp.", 878.35, 12.67, 1.46, 43567890L, "NASDAQ"));
        sampleStocks.put("JPM", new Stock("JPM", "JPMorgan Chase & Co.", 252.18, 1.89, 0.76, 8901234L, "NYSE"));
        sampleStocks.put("V", new Stock("V", "Visa Inc.", 318.45, 2.34, 0.74, 5678901L, "NYSE"));
        sampleStocks.put("JNJ", new Stock("JNJ", "Johnson & Johnson", 158.92, -0.87, -0.54, 6789012L, "NYSE"));
        sampleStocks.put("WMT", new Stock("WMT", "Walmart Inc.", 92.34, 1.23, 1.35, 7234567L, "NYSE"));
        sampleStocks.put("PG", new Stock("PG", "Procter & Gamble Co.", 168.47, 0.89, 0.53, 3456789L, "NYSE"));
        sampleStocks.put("KO", new Stock("KO", "The Coca-Cola Co.", 72.58, -0.45, -0.62, 4567890L, "NYSE"));
        sampleStocks.put("MCD", new Stock("MCD", "McDonald's Corp.", 298.76, 2.12, 0.72, 2345678L, "NYSE"));
        sampleStocks.put("DIS", new Stock("DIS", "The Walt Disney Co.", 92.45, 1.87, 2.07, 8765432L, "NYSE"));
        sampleStocks.put("BA", new Stock("BA", "The Boeing Co.", 198.32, -3.45, -1.71, 5432109L, "NYSE"));
        sampleStocks.put("GE", new Stock("GE", "General Electric Co.", 104.56, 2.34, 2.29, 6543210L, "NYSE"));
        sampleStocks.put("F", new Stock("F", "Ford Motor Co.", 12.89, 0.34, 2.71, 123456789L, "NYSE"));
        sampleStocks.put("GM", new Stock("GM", "General Motors Co.", 45.67, 1.23, 2.77, 98765432L, "NYSE"));
        sampleStocks.put("CSCO", new Stock("CSCO", "Cisco Systems Inc.", 52.34, -0.67, -1.27, 21098765L, "NASDAQ"));
        sampleStocks.put("ORCL", new Stock("ORCL", "Oracle Corp.", 189.45, 3.21, 1.72, 9876543L, "NYSE"));
        sampleStocks.put("SAP", new Stock("SAP", "SAP SE", 198.76, 2.15, 1.09, 4321098L, "NYSE"));
        sampleStocks.put("IBM", new Stock("IBM", "International Business Machines", 178.23, -1.45, -0.81, 3210987L, "NYSE"));
        sampleStocks.put("INTC", new Stock("INTC", "Intel Corp.", 32.87, -0.98, -2.90, 54321098L, "NASDAQ"));
        sampleStocks.put("AMD", new Stock("AMD", "Advanced Micro Devices Inc.", 182.45, 5.67, 3.21, 32109876L, "NASDAQ"));
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        for (String symbol : popularStocks) {
            try {
                stocks.add(getStockBySymbol(symbol));
            } catch (Exception e) {
                // Log the error and use sample data as fallback
                System.err.println("Warning: Failed to fetch stock " + symbol + ": " + e.getMessage());
                Stock sample = sampleStocks.get(symbol);
                if (sample != null) {
                    stocks.add(sample);
                }
            }
        }
        // If all API calls failed, return sample data
        if (stocks.isEmpty()) {
            return new ArrayList<>(sampleStocks.values());
        }
        return stocks;
    }

    public Stock getStockBySymbol(String symbol) {
        String upperSymbol = symbol.toUpperCase();
        
        // Check cache first
        CachedStock cached = cache.get(upperSymbol);
        if (cached != null && Instant.now().minusSeconds(cacheTtlSeconds).isBefore(cached.timestamp)) {
            return cached.stock;
        }

        // If no API key, return sample data
        if (apiKey == null || apiKey.isBlank()) {
            Stock sample = sampleStocks.get(upperSymbol);
            if (sample != null) {
                return sample;
            }
            throw new IllegalStateException("Stock API key not configured and no sample data for: " + upperSymbol);
        }

        try {
            // Use Alpha Vantage GLOBAL_QUOTE endpoint for latest price
            String uri = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", baseUrl, upperSymbol, apiKey);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Empty response from Alpha Vantage API");
            }

            // Check for rate limit message
            if (response.containsKey("Note") || response.containsKey("Information")) {
                System.err.println("Alpha Vantage rate limit hit, using sample data");
                Stock sample = sampleStocks.get(upperSymbol);
                if (sample != null) {
                    return sample;
                }
                throw new RuntimeException("Rate limit exceeded and no sample data for: " + upperSymbol);
            }

            if (response.get("Global Quote") == null) {
                throw new RuntimeException("Invalid response from Alpha Vantage API for symbol: " + upperSymbol);
            }

            @SuppressWarnings("unchecked")
            Map<String, String> quote = (Map<String, String>) response.get("Global Quote");

            if (quote.isEmpty()) {
                throw new RuntimeException("Empty quote data for symbol: " + upperSymbol);
            }

            String priceStr = quote.getOrDefault("05. price", "0");
            if (priceStr.equals("0") || priceStr.isEmpty()) {
                throw new RuntimeException("No price data available for symbol: " + upperSymbol);
            }

            String changeStr = quote.getOrDefault("09. change", "0");
            String changePercentStr = quote.getOrDefault("10. change percent", "0%");
            String volumeStr = quote.getOrDefault("06. volume", "0");

            double price = parseDoubleSafe(priceStr);
            double change = parseDoubleSafe(changeStr);
            double changePercent = parsePercentSafe(changePercentStr);
            long volume = parseLongSafe(volumeStr);

            String name = sampleStocks.containsKey(upperSymbol) ? sampleStocks.get(upperSymbol).getName() : upperSymbol;
            String exchange = sampleStocks.containsKey(upperSymbol) ? sampleStocks.get(upperSymbol).getExchange() : "NYSE";
            
            Stock stock = new Stock(upperSymbol, name, price, change, changePercent, volume, exchange);
            cache.put(upperSymbol, new CachedStock(stock, Instant.now()));
            return stock;
        } catch (Exception e) {
            // Fallback to sample data on any error
            Stock sample = sampleStocks.get(upperSymbol);
            if (sample != null) {
                System.err.println("API failed for " + upperSymbol + ", using sample data: " + e.getMessage());
                return sample;
            }
            throw new RuntimeException("Failed to fetch stock data for symbol: " + upperSymbol + ". Error: " + e.getMessage(), e);
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



