package com.edustocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {
    private String symbol;
    private int quantity;
    private double averagePrice;
    private double currentPrice;
    private double totalValue;
    private double profit;
    private double profitPercent;
}



