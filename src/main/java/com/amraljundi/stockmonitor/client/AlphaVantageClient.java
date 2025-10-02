package com.amraljundi.stockmonitor.client;

import com.amraljundi.stockmonitor.exception.StockApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AlphaVantageClient {
    private static final Logger log = LoggerFactory.getLogger(AlphaVantageClient.class);

    private final String apiKey;
    private final String baseUrl;
    private final RestTemplate restTemplate;


    public AlphaVantageClient(
            @Value("${alphavantage.api.key}") String apiKey,
            @Value("${alphavantage.api.base-url}") String baseUrl
    ) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, String> getGlobalQuote(String symbol) {
        String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", baseUrl, symbol, apiKey);

        log.debug("Fetching quote for symbol: {}", symbol);
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                log.error("Received null response for symbol: {}", symbol);
                throw new StockApiException("Null response from API for symbol: " + symbol);
            }

            if (response.containsKey("Error Message")) {
                String errorMessage = (String) response.get("Error Message");
                log.error("API error for symbol {}: {}", symbol, errorMessage);
                throw new StockApiException("API error: " + errorMessage);
            }

            if (response.containsKey("Note")) {
                String note = (String) response.get("Note");
                log.warn("API rate limit for symbol {}: {}", symbol, note);
                throw new StockApiException("API rate limit: " + note);
            }

            if (!response.containsKey("Global Quote")) {
                log.error("Response missing 'Global Quote' for symbol: {}", symbol);
                throw new StockApiException("Invalid response structure for symbol: " + symbol);
            }

            Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
            if (quote == null || quote.isEmpty()) {
                log.error("Empty quote data for symbol: {}", symbol);
                throw new StockApiException("Empty quote data for symbol: " + symbol);
            }

            log.debug("Successfully fetched quote for symbol: {}", symbol);
            return quote;
        } catch (RestClientException e) {
            log.error("REST client error fetching symbol {}: {}", symbol, e.getMessage());
            throw new StockApiException("Failed to fetch data for symbol: " + symbol, e);
        }
    }
}
