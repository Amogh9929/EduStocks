package com.edustocks.service;

import com.edustocks.model.Portfolio;
import com.edustocks.model.Holding;
import com.edustocks.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private Portfolio testPortfolio;
    private String testUserId = "test-user-123";

    @BeforeEach
    void setUp() {
        testPortfolio = new Portfolio();
        testPortfolio.setUserId(testUserId);
        testPortfolio.setCash(10000.0);
        testPortfolio.setHoldings(new ArrayList<>());
    }

    @Test
    void testGetPortfolio_WhenExists() {
        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testPortfolio));

        Portfolio result = portfolioService.getPortfolio(testUserId);

        assertNotNull(result);
        assertEquals(testUserId, result.getUserId());
        assertEquals(10000.0, result.getCash());
        verify(portfolioRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void testGetPortfolio_WhenNotExists_CreatesNew() {
        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(Optional.empty());
        when(portfolioRepository.save(any(Portfolio.class)))
            .thenReturn(testPortfolio);

        Portfolio result = portfolioService.getPortfolio(testUserId);

        assertNotNull(result);
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testBuyStock_Success() {
        Holding holding = new Holding();
        holding.setSymbol("AAPL");
        holding.setQuantity(10);
        holding.setAveragePrice(150.0);

        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testPortfolio));
        when(portfolioRepository.save(any(Portfolio.class)))
            .thenReturn(testPortfolio);

        portfolioService.buyStock(testUserId, "AAPL", 10, 150.0);

        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testSellStock_Success() {
        Holding holding = new Holding();
        holding.setSymbol("AAPL");
        holding.setQuantity(10);
        holding.setAveragePrice(150.0);
        testPortfolio.getHoldings().add(holding);

        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testPortfolio));
        when(portfolioRepository.save(any(Portfolio.class)))
            .thenReturn(testPortfolio);

        portfolioService.sellStock(testUserId, "AAPL", 5, 160.0);

        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testCalculateTotalValue() {
        Holding holding1 = new Holding();
        holding1.setSymbol("AAPL");
        holding1.setQuantity(10);
        holding1.setCurrentPrice(150.0);

        Holding holding2 = new Holding();
        holding2.setSymbol("GOOGL");
        holding2.setQuantity(5);
        holding2.setCurrentPrice(200.0);

        testPortfolio.getHoldings().add(holding1);
        testPortfolio.getHoldings().add(holding2);

        double expectedValue = 10000.0 + (10 * 150.0) + (5 * 200.0); // cash + stocks

        when(portfolioRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testPortfolio));

        double totalValue = portfolioService.calculateTotalValue(testUserId);

        assertEquals(expectedValue, totalValue, 0.01);
    }
}
