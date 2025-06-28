package com.amraljundi.stockmonitor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Momentum(
        String symbol,
        BigDecimal value,
        BigDecimal percentChange,
        int periodSize,
        LocalDateTime calculatedAt
) {

    // Momentum direction enum
    public enum Direction {
        BULLISH,    // Positive momentum (price going up)
        BEARISH,    // Negative momentum (price going down)
        NEUTRAL     // No significant change
    }

    public Momentum {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be null or blank");
        }
        if (periodSize <= 0) {
            throw new IllegalArgumentException("Period size must be positive");
        }
        if (calculatedAt == null) {
            calculatedAt = LocalDateTime.now();
        }

        symbol = symbol.toUpperCase();
    }

    public Momentum(String symbol, BigDecimal value, int periodSize) {
        this(symbol, value, null, periodSize, LocalDateTime.now());
    }

    public Direction direction() {
        if (value == null) return Direction.NEUTRAL;

        int comparison = value.compareTo(BigDecimal.ZERO);
        if (comparison > 0) return Direction.BULLISH;
        if (comparison < 0) return Direction.BEARISH;
        return Direction.NEUTRAL;
    }
}
