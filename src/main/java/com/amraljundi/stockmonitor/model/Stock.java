package com.amraljundi.stockmonitor.model;

import java.time.LocalDateTime;

public class Stock {
    private String symbol;
    private String companyName;
    private boolean active;
    private LocalDateTime createdAt;

    public Stock() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public Stock(String symbol, String companyName) {
        this();
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = (symbol != null) ? symbol.toUpperCase() : null;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Stock{symbol='" + symbol + "', companyName='" + companyName + "'}";
    }
}
