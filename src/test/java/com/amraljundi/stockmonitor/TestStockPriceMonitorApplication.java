package com.amraljundi.stockmonitor;

import org.springframework.boot.SpringApplication;

public class TestStockPriceMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.from(StockPriceMonitorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
