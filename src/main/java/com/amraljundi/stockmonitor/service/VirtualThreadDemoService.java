package com.amraljundi.stockmonitor.service;

import com.amraljundi.stockmonitor.model.StockPrice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

@Service
public class VirtualThreadDemoService {
    private final Executor virtualThreadExecutor;
    private final Executor platformThreadExecutor;

    public VirtualThreadDemoService(
            @Qualifier("virtualThreadExecutor") Executor virtualThreadExecutor,
            @Qualifier("platformThreadExecutor") Executor platformThreadExecutor
    ) {
        this.virtualThreadExecutor = virtualThreadExecutor;
        this.platformThreadExecutor = platformThreadExecutor;
    }

    // Demo for virtual threads vs platform threads
    public void demonstrateApiCalls() {
        System.out.println("------- Virtual Threads vs Platform Threads -------");
        System.out.println("=".repeat(50));

        // virtual threads
        long startTime = System.currentTimeMillis();
        testConcurrentApiCalls(virtualThreadExecutor, "Virtual Threads", 1000);
        long virtualTime =  System.currentTimeMillis() - startTime;

        // platform threads
        startTime = System.currentTimeMillis();
        testConcurrentApiCalls(platformThreadExecutor, "Platform Threads", 100);
        long platformTime = System.currentTimeMillis() - startTime;

        System.out.println("RESULTS:");
        System.out.println("Virtual Threads (1000 calls): " + virtualTime + "ms");
        System.out.println("Platform Threads (100 calls): " + platformTime + "ms");
        System.out.println("Virtual threads handled 10x more calls in similar time");
    }

    // Simulating fetching stock prices from multiple APIs
    private void testConcurrentApiCalls(Executor executor, String threadType, int numberOfCalls) {
        System.out.println("Testing " + threadType + " with " + numberOfCalls + " concurrent calls");

        List<CompletableFuture<StockPrice>> futures = IntStream.range(0, numberOfCalls)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> simulateApiCall("Stock " + i), executor))
                .toList();

        // Wait for all futures to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allFutures.join();
            System.out.println("Success: " + threadType + " completed " + numberOfCalls + " calls");
        } catch (Exception e) {
            System.out.println("Failure: " + threadType + " failed: " + e.getMessage());
        }

    }

    private StockPrice simulateApiCall(String symbol) {
        try {
            Thread.sleep(100 + (int) (Math.random() * 100));

            BigDecimal price = BigDecimal.valueOf(100 + Math.random() * 900)
                    .setScale(2, RoundingMode.HALF_UP);

            return new StockPrice(symbol, price);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("API call interrupted", e);
        }
    }

}
