package com.mattos.exercise.utils;

import com.mattos.exercise.domain.RewardScheme;
import com.mattos.exercise.domain.SalesOrder;

import java.util.ArrayList;
import java.util.List;

public  class StubDataRepository {

    public static SalesOrder getRandomSalesOrderInstance(String category){

        return SalesOrder.getBuilderByCategory(category).withCategory(category)
                .withSale(null, 10.0D, RewardScheme.BLUE, null, null).build();

    }

    public static List<SalesOrder> getSalesOrderInstancesDifferentPrice(String category, Double... price){
        List<SalesOrder> stubbedList = new ArrayList<>();
        for (int i = 0; i < price.length; i++) {
            stubbedList.add(SalesOrder.getBuilderByCategory(category).withCategory(category)
                    .withSale(null, price[i], RewardScheme.BLUE, null, null).build());
        }

        return stubbedList;
    }
}
