package com.edustocks.controller;

import com.edustocks.model.Stock;
import com.edustocks.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStock(@PathVariable String symbol) {
        Stock stock = stockService.getStockBySymbol(symbol);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Stock>> searchStocks(@RequestParam String query) {
        List<Stock> stocks = stockService.searchStocks(query);
        return ResponseEntity.ok(stocks);
    }
}



