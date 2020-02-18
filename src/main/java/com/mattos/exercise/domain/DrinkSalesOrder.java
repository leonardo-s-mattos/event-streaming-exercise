package com.mattos.exercise.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkSalesOrder extends SalesOrder{

    private String size;

    public String getSize() {
        return size;
    }

    public static class Builder extends SalesOrderBuilder {

        public DrinkSalesOrder build() {

            DrinkSalesOrder result = new DrinkSalesOrder();
            result.id = id;
            result.rewardScheme = rewardScheme;
            result.category = category;
            result.price = price;
            result.name = name;
            result.size = size;
            return result;
        }
    }

}
