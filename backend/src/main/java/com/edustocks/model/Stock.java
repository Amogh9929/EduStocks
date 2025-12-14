package com.edustocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changePercent;
    private long volume;
    private String exchange;
}



