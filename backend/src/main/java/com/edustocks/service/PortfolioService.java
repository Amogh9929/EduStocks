package com.edustocks.service;

import com.edustocks.model.Holding;
import com.edustocks.model.Portfolio;
import com.edustocks.model.Stock;
import com.edustocks.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService;

    public Portfolio getPortfolio(String userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio == null) {
            portfolio = new Portfolio(userId);
            portfolioRepository.save(portfolio);
        }
        
        // Update current prices and recalculate values
        updatePortfolioValues(portfolio);
        return portfolio;
    }

    public void buyStock(String userId, String symbol, int quantity) {
        Portfolio portfolio = getPortfolio(userId);
        Stock stock = stockService.getStockBySymbol(symbol);
        
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }

        double totalCost = stock.getPrice() * quantity;
        
        if (portfolio.getBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");
        }

        // Find existing holding
        Holding existingHolding = portfolio.getHoldings().stream()
            .filter(h -> h.getSymbol().equals(symbol))
            .findFirst()
            .orElse(null);

        if (existingHolding != null) {
            // Update existing holding
            int newQuantity = existingHolding.getQuantity() + quantity;
            double newAveragePrice = ((existingHolding.getAveragePrice() * existingHolding.getQuantity()) + totalCost) / newQuantity;
            existingHolding.setQuantity(newQuantity);
            existingHolding.setAveragePrice(newAveragePrice);
            existingHolding.setCurrentPrice(stock.getPrice());
            existingHolding.setTotalValue(newQuantity * stock.getPrice());
        } else {
            // Create new holding
            Holding newHolding = new Holding(
                symbol,
                quantity,
                stock.getPrice(),
                stock.getPrice(),
                quantity * stock.getPrice(),
                0,
                0
            );
            portfolio.getHoldings().add(newHolding);
        }

        portfolio.setBalance(portfolio.getBalance() - totalCost);
        updatePortfolioValues(portfolio);
        portfolioRepository.save(portfolio);
    }

    public void sellStock(String userId, String symbol, int quantity) {
        Portfolio portfolio = getPortfolio(userId);
        Stock stock = stockService.getStockBySymbol(symbol);
        
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }

        Holding holding = portfolio.getHoldings().stream()
            .filter(h -> h.getSymbol().equals(symbol))
            .findFirst()
            .orElse(null);

        if (holding == null || holding.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient shares");
        }

        double totalValue = stock.getPrice() * quantity;
        portfolio.setBalance(portfolio.getBalance() + totalValue);

        if (holding.getQuantity() == quantity) {
            portfolio.getHoldings().remove(holding);
        } else {
            holding.setQuantity(holding.getQuantity() - quantity);
            holding.setCurrentPrice(stock.getPrice());
            holding.setTotalValue(holding.getQuantity() * stock.getPrice());
        }

        updatePortfolioValues(portfolio);
        portfolioRepository.save(portfolio);
    }

    private void updatePortfolioValues(Portfolio portfolio) {
        double holdingsValue = 0;
        
        for (Holding holding : portfolio.getHoldings()) {
            Stock stock = stockService.getStockBySymbol(holding.getSymbol());
            if (stock != null) {
                holding.setCurrentPrice(stock.getPrice());
                holding.setTotalValue(holding.getQuantity() * stock.getPrice());
                holding.setProfit(holding.getTotalValue() - (holding.getAveragePrice() * holding.getQuantity()));
                holding.setProfitPercent((holding.getProfit() / (holding.getAveragePrice() * holding.getQuantity())) * 100);
            }
            holdingsValue += holding.getTotalValue();
        }
        
        portfolio.setTotalValue(portfolio.getBalance() + holdingsValue);
    }
}



