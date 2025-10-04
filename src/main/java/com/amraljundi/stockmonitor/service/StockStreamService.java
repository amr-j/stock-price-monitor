package com.amraljundi.stockmonitor.service;

import com.amraljundi.stockmonitor.model.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockStreamService {
    private static final Logger log = LoggerFactory.getLogger(StockStreamService.class);

    public List<StockPrice> filterAbovePrice(List<StockPrice> prices, BigDecimal threshold) {
        return prices.stream()
                .filter(stockPrice -> stockPrice.price().compareTo(threshold) > 0)
                .toList();
    }

    public List<BigDecimal> extractPrices(List<StockPrice> prices) {
        return prices.stream()
                .map(StockPrice::price)
                .toList();
    }

    public Map<String, List<StockPrice>> groupBySymbol(List<StockPrice> prices) {
        return prices.stream()
                .collect(Collectors.groupingBy(StockPrice::symbol));
    }

    public List<StockPrice> findTopNExpensive(List<StockPrice> prices, int n) {
        return prices.stream()
                .sorted(Comparator.comparing(StockPrice::price).reversed())
                .limit(n)
                .toList();
    }

}
