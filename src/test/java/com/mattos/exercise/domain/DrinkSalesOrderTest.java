package com.mattos.exercise.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class DrinkSalesOrderTest {

    private SalesOrder stubOrder;
    private String stubOrderAsJson = "{\"id\":\"124\", \"category\": \"drink\", \"price\": 15, \"rewards_scheme\": \"green\" , \"name\": \"diet coke\", \"size\": \"large\"}";

    @Before
    public void init(){
        stubOrder = new DrinkSalesOrder.Builder().withId("124").withCategory("drink")
                .withSale("diet coke", 15D, RewardScheme.GREEN, "large", null).build();
    }

    @Test
    public void givenAnInstanceOfOrder_whenSerialize_AllValuesAreCorrectInString() throws JsonProcessingException {

        String actual = DrinkSalesOrder.toJson(stubOrder);
        Assert.assertTrue(actual.contains("15"));
        Assert.assertTrue(actual.contains("GREEN"));
        Assert.assertTrue(actual.contains("\"size\":\"large\""));

    }

    @Test
    public void givenAJSOn_whenDeserialize_AllValuesAreCorrectInObject() throws IOException {

        DrinkSalesOrder actual = DrinkSalesOrder.fromJson(stubOrderAsJson);
        Assert.assertTrue("drink".equals(actual.getCategory()));
        Assert.assertTrue("large".equals(actual.getSize()));

    }
}
