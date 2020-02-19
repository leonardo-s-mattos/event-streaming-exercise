package com.mattos.exercise.producer.service;

import com.mattos.exercise.domain.RewardScheme;
import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.domain.SalesOrderBuilder;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class RandomSalesOrderGenerator {

    private static String[] categories = {"sandwich","drink","retail"};
    private static String[] rewardsSchemes = {"GREEN","BLUE","YELLOW"};
    private static String[] sizes = {"small","medium","large"};

    public static SalesOrder getNewRandomSalesOrder(){

        String randomCategory = categories[getRandomNumberInRange(1,3)-1];

        SalesOrderBuilder builder = SalesOrder.getBuilderByCategory(randomCategory);

        return builder.withCategory(randomCategory)
                    .withId(UUID.randomUUID().toString())
                    .withSale("me", getRandomNumber()
                            , RewardScheme.valueOf(rewardsSchemes[getRandomNumberInRange(1, 3) - 1])
                            , sizes[getRandomNumberInRange(1, 3) - 1]
                            , Arrays.asList("lettuce", "mayo"))
                    .build();

    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static Double getRandomNumber() {
       return Precision.round(new Random().nextDouble() * 100.D,2);
    }





}
