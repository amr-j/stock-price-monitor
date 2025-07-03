package com.amraljundi.stockmonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class VirtualThreadConfig {

    @Bean(name = "virtualThreadExecutor")
    public Executor virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean(name = "platformThreadExecutor")
    public Executor platformThreadExecutor() {
        return Executors.newFixedThreadPool(20); // limited pool
    }

    // For debugging
    @Bean
    public Thread.Builder.OfVirtual virtualThreadFactory() {
        return Thread.ofVirtual()
                .name("stock-monitor-vt-", 0);
    }
}
