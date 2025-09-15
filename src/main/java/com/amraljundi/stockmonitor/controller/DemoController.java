package com.amraljundi.stockmonitor.controller;

import com.amraljundi.stockmonitor.service.VirtualThreadDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller to demonstrate Virtual Threads in action
 * Hit these endpoints to see the magic happen!
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final VirtualThreadDemoService demoService;

    public DemoController(VirtualThreadDemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/performance")
    public String demonstratePerformance() {
        demoService.demonstrateApiCalls();
        return "Check the console for performance comparison results";
    }
}
