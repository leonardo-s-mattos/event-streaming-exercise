package com.mattos.exercise.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetailSalesOrder extends SalesOrder{

    public static class Builder extends SalesOrderBuilder {

        public RetailSalesOrder build() {

            RetailSalesOrder result = new RetailSalesOrder();
            result.id = id;
            result.rewardScheme = rewardScheme;
            result.category = category;
            result.price = price;
            result.name = name;
            return result;
        }
    }

}
