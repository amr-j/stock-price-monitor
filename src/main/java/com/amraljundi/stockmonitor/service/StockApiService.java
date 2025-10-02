package com.amraljundi.stockmonitor.service;

import com.amraljundi.stockmonitor.client.AlphaVantageClient;
import com.amraljundi.stockmonitor.exception.StockApiException;
import com.amraljundi.stockmonitor.model.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    private static final Logger log = LoggerFactory.getLogger(StockApiService.class);

    private final Executor virtualThreadsExecutor;
    private final AlphaVantageClient apiClient;

    public StockApiService(
            @Qualifier("virtualThreadExecutor") Executor virtualThreadExecutor,
            AlphaVantageClient apiClient
    ) {
        this.virtualThreadsExecutor = virtualThreadExecutor;
        this.apiClient = apiClient;
    }

    public StockPrice fetchStockPrice(String symbol) {
        try {
            log.info("Fetching stock price for symbol: {}", symbol);

            Map<String, String> quote = apiClient.getGlobalQuote(symbol);
            StockPrice stockPrice = parseStockResponse(symbol, quote);
            log.info("Successfully fetched price for {}", symbol);

            return stockPrice;
        } catch (StockApiException e) {
            log.error("API error fetching {}: {}", symbol, e.getMessage());
            return createFallbackPrice(symbol);
        }
    }

    // TODO - why not fetch in a batch?
    public List<StockPrice> fetchStocksConcurrently(List<String> symbols) {

        List<CompletableFuture<StockPrice>> futures = symbols.stream()
                .map(symbol -> CompletableFuture.supplyAsync(() -> fetchStockPrice(symbol), virtualThreadsExecutor))
                .toList();

        List<StockPrice> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        return results;
    }

    @SuppressWarnings("unchecked")
    private StockPrice parseStockResponse(String symbol, Map<String, String> quote) {
        String priceStr = quote.get("05. price");
        BigDecimal price = new BigDecimal(priceStr);

        log.debug("Parsed price for {}: {}", symbol, price);
        return new StockPrice(symbol, price);
    }

    private StockPrice createFallbackPrice(String symbol) {
        log.warn("Using fallback price for symbol: {}", symbol);
        return new StockPrice(symbol, BigDecimal.valueOf(100.00));
    }
}
