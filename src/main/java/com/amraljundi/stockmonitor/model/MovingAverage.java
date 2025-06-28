package com.amraljundi.stockmonitor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovingAverage(
        String symbol,
        BigDecimal value,
        int periodSize,
        LocalDateTime calculatedAt
) {

    public MovingAverage {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be null or blank");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (periodSize <= 0) {
            throw new IllegalArgumentException("Period size must be positive");
        }
        if (calculatedAt == null) {
            calculatedAt = LocalDateTime.now();
        }

        symbol = symbol.toUpperCase();
    }

    public MovingAverage(String symbol, BigDecimal value, int periodSize) {
        this(symbol, value, periodSize, LocalDateTime.now());
    }
}