package com.edustocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    private String userId;
    private double balance;
    private List<Holding> holdings;
    private double totalValue;

    public Portfolio(String userId) {
        this.userId = userId;
        this.balance = 10000.0; // Starting balance
        this.holdings = new ArrayList<>();
        this.totalValue = balance;
    }
}



