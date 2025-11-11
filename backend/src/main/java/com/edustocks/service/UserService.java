package com.edustocks.service;

import com.edustocks.repository.UserProgressRepository;
import com.edustocks.repository.PortfolioRepository;
import com.edustocks.model.UserProgress;
import com.edustocks.model.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    public void initializeUser(String userId, String email) {
        // Initialize user progress if doesn't exist
        UserProgress progress = userProgressRepository.findByUserId(userId);
        if (progress == null) {
            progress = new UserProgress(userId);
            userProgressRepository.save(progress);
        }

        // Initialize portfolio if doesn't exist
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio == null) {
            portfolio = new Portfolio(userId);
            portfolioRepository.save(portfolio);
        }
    }
}



