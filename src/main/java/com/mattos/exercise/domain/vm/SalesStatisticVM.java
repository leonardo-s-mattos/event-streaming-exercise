package com.mattos.exercise.domain.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SalesStatisticVM {

    private String category;
    private Double averagePrice;

    public SalesStatisticVM(String category, Double averagePrice, Double averagePoints) {
        this.category = category;
        this.averagePrice = averagePrice;
        this.averagePoints = averagePoints;
    }

    public String getCategory() {
        return category;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public Double getAveragePoints() {
        return averagePoints;
    }

    private Double averagePoints;
}
