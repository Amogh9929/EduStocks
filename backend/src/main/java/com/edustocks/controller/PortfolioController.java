package com.edustocks.controller;

import com.edustocks.model.Portfolio;
import com.edustocks.service.PortfolioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<Portfolio> getPortfolio(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Portfolio portfolio = portfolioService.getPortfolio(userId);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/buy")
    public ResponseEntity<Map<String, Object>> buyStock(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String symbol = (String) requestBody.get("symbol");
        Integer quantity = (Integer) requestBody.get("quantity");

        try {
            portfolioService.buyStock(userId, symbol, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Stock purchased successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<Map<String, Object>> sellStock(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String symbol = (String) requestBody.get("symbol");
        Integer quantity = (Integer) requestBody.get("quantity");

        try {
            portfolioService.sellStock(userId, symbol, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Stock sold successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}



