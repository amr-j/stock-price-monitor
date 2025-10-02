package com.amraljundi.stockmonitor.controller;

import com.amraljundi.stockmonitor.model.StockPrice;
import com.amraljundi.stockmonitor.service.StockApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StocksController {
    private final StockApiService stockApiService;

    public StocksController(StockApiService stockApiService) {
        this.stockApiService = stockApiService;
    }

    @GetMapping("/fetch")
    public List<StockPrice> fetchStocks(@RequestParam List<String> symbols) {
        return stockApiService.fetchStocksConcurrently(symbols);
    }

}
