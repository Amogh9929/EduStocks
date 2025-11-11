package com.edustocks.repository;

import com.edustocks.model.Portfolio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PortfolioRepository {
    
    // In-memory storage - replace with Firebase Firestore in production
    private final Map<String, Portfolio> portfolios = new HashMap<>();

    public Portfolio findByUserId(String userId) {
        return portfolios.get(userId);
    }

    public void save(Portfolio portfolio) {
        portfolios.put(portfolio.getUserId(), portfolio);
    }

    public List<Portfolio> findAll() {
        return new ArrayList<>(portfolios.values());
    }

    public void delete(String userId) {
        portfolios.remove(userId);
    }
}



