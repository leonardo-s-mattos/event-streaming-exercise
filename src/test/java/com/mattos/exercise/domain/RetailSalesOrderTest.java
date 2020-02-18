package com.mattos.exercise.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class RetailSalesOrderTest {

    private SalesOrder stubOrder;
    private String stubOrderAsJson = "{\"id\":\"124\",\"category\":\"retail\",\"price\":15.0,\"name\":\"turkey sub\",\"addons\":[\"onions\",\"lettuce\"],\"rewards_scheme\":\"GREEN\"}";

    @Before
    public void init(){
        stubOrder = new RetailSalesOrder.Builder().withId("124").withCategory("retail")
                .withSale("turkey sub", 15D, RewardScheme.GREEN, null, null).build();
    }

    @Test
    public void givenAnInstanceOfOrder_whenSerialize_AllValuesAreCorrectInString() throws JsonProcessingException {

        String actual = DrinkSalesOrder.toJson(stubOrder);
        Assert.assertTrue(actual.contains("15"));
        Assert.assertTrue(actual.contains("GREEN"));
        Assert.assertTrue(!actual.contains("onions"));
        Assert.assertTrue(!actual.contains("\"size\":\"large\""));
        
    }

    @Test
    public void givenAJSOn_whenDeserialize_AllValuesAreCorrectInObject() throws IOException {

        RetailSalesOrder actual = RetailSalesOrder.fromJson(stubOrderAsJson);
        Assert.assertTrue("retail".equals(actual.getCategory()));

    }
}
