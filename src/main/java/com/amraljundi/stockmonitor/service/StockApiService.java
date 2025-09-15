package com.amraljundi.stockmonitor.service;

import com.amraljundi.stockmonitor.model.StockPrice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Service to fetch real stock data from Alpha Vantage API using Virtual Threads
 * The purpose is to learn concurrent API calls using virtual threads
 */
@Service
public class StockApiService {
    private final String apiKey;
    private final String baseUrl;
    private final Executor virtualThreadsExecutor;
    private final RestTemplate restTemplate;

    public StockApiService(
            @Value("${alphavantage.api.key}") String apiKey,
            @Value("${alphavantage.api.base-url}") String baseUrl,
            @Qualifier("virtualThreadsExecutor") Executor virtualThreadExecutor
    ) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.virtualThreadsExecutor = virtualThreadExecutor;
        this.restTemplate = new RestTemplate();
    }

    public StockPrice fetchStockPrice(String symbol) {
        try {
            // TODO - Add info logging here
            String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apiKey=%s", baseUrl, symbol, apiKey);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // TODO - replace with a client and proper response deserialisation
            return parseStockResponse(symbol, response);
        } catch (Exception e) {
            // TODO - replace with a logger
            System.err.println("Error fetching " + symbol + ": " + e.getMessage());
            // Return a fallback price for demo purposes
            return new StockPrice(symbol, BigDecimal.valueOf(100.00));
        }
    }

    public List<StockPrice> fetchStocksConcurrently(List<String> symbols) {
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<StockPrice>> futures = symbols.stream()
                .map(symbol -> CompletableFuture.supplyAsync(() -> fetchStockPrice(symbol), virtualThreadsExecutor))
                .toList();

        List<StockPrice> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Fetched " + symbols.size() + " stocks in " + duration + "ms using Virtual Threads!");

        return results;
    }

    @SuppressWarnings("unchecked")
    private StockPrice parseStockResponse(String symbol, Map<String, Object> response) {
        try {
            Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
            if (quote == null || quote.isEmpty()) {
                System.out.println("No quote data for " + symbol + " - might be API rate limit");
                return new StockPrice(symbol, BigDecimal.valueOf(100.00));
            }

            String priceStr = quote.get("05. price");
            BigDecimal price = new BigDecimal(priceStr);

            return new StockPrice(symbol, price);
        } catch (Exception e) {
            // TODO - replace with a logger
            System.err.println("Error parsing response for " + symbol + ": " + e.getMessage());
            return new StockPrice(symbol, BigDecimal.valueOf(100.00));
        }
    }


    // Demo method: Compare sequential vs concurrent fetching
    public void demonstrateSequentialVsConcurrent(List<String> symbols) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("SEQUENTIAL vs CONCURRENT API CALLS");
        System.out.println("=".repeat(50));

        // Sequential fetching
        long startTime = System.currentTimeMillis();
        for (String symbol : symbols) {
            fetchStockPrice(symbol);
        }
        long sequentialTime = System.currentTimeMillis() - startTime;
        System.out.println("🐌 Sequential: " + sequentialTime + "ms");

        // Wait to avoid hitting API rate limits
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        // Concurrent fetching
        startTime = System.currentTimeMillis();
        fetchStocksConcurrently(symbols);
        long concurrentTime = System.currentTimeMillis() - startTime;
        System.out.println("⚡ Concurrent: " + concurrentTime + "ms");

        System.out.println("Speed improvement: " + (sequentialTime / (double) concurrentTime) + "x faster!");
    }
}
