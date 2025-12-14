package com.edustocks.service;

import com.edustocks.model.Portfolio;
import com.edustocks.model.Holding;
import com.edustocks.model.Stock;
import com.edustocks.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private PortfolioService portfolioService;

    private Portfolio testPortfolio;
    private String testUserId = "test-user-123";

    @BeforeEach
    void setUp() {
        testPortfolio = new Portfolio(testUserId);
    }

    @Test
    void testGetPortfolio_WhenExists() {
        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(testPortfolio);

        Portfolio result = portfolioService.getPortfolio(testUserId);

        assertNotNull(result);
        assertEquals(testUserId, result.getUserId());
        assertEquals(10000.0, result.getBalance());
        verify(portfolioRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void testGetPortfolio_WhenNotExists_CreatesNew() {
        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(null);

        Portfolio result = portfolioService.getPortfolio(testUserId);

        assertNotNull(result);
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testBuyStock_Success() {
        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(testPortfolio);
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setName("Apple Inc.");
        stock.setPrice(150.0);
        when(stockService.getStockBySymbol("AAPL")).thenReturn(stock);

        portfolioService.buyStock(testUserId, "AAPL", 10);

        verify(portfolioRepository, atLeastOnce()).save(any(Portfolio.class));
    }

    @Test
    void testSellStock_Success() {
        Holding holding = new Holding();
        holding.setSymbol("AAPL");
        holding.setQuantity(10);
        holding.setAveragePrice(150.0);
        testPortfolio.getHoldings().add(holding);

        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(testPortfolio);
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setName("Apple Inc.");
        stock.setPrice(160.0);
        when(stockService.getStockBySymbol("AAPL")).thenReturn(stock);

        portfolioService.sellStock(testUserId, "AAPL", 5);

        verify(portfolioRepository, atLeastOnce()).save(any(Portfolio.class));
    }

}

