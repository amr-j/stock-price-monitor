package com.amraljundi.stockmonitor.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record StockPrice(
        String symbol,
        BigDecimal price,
        LocalDateTime timestamp,
        Long volume
) {

    public StockPrice {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be null or blank");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }

        symbol = symbol.toUpperCase();
    }

    public StockPrice(String symbol, BigDecimal price) {
        this(symbol, price, LocalDateTime.now(), null);
    }

    // Methods for stream processing
    public boolean isNewerThan(StockPrice other) {
        return this.timestamp.isAfter(other.timestamp);
    }

    public boolean isSameSymbol(StockPrice other) {
        return this.symbol.equals(other.symbol);
    }

    // Calculate price change from another price point
    public BigDecimal calculateChangeFrom(StockPrice previousPrice) {
        if (previousPrice == null || previousPrice.price == null) {
            return BigDecimal.ZERO;
        }
        return this.price.subtract(previousPrice.price);
    }

    public BigDecimal calculatePercentageFrom(StockPrice previousPrice) {
        if (previousPrice == null || previousPrice.price == null
                || (previousPrice.price.compareTo(BigDecimal.ZERO) == 0)) {
            return BigDecimal.ZERO;
        }
        BigDecimal change = calculateChangeFrom(previousPrice);

        return change.divide(previousPrice.price, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

}
