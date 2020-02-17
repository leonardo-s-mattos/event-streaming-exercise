package com.mattos.exercise.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class OrderTest {

    private Order stubOrder;
    private String stubOrderAsJson = "{\"id\":124, \"category\": \"sandwich\", \"price\": 15, \"rewards_scheme\": \"green\" , \"name\": \"turkey sub\"}";

    @Before
    public void init(){
        stubOrder = new Order.Builder().withId("124").withCategory("sandwich")
                .withSale("turkey sub", 15D, "green", null, new String[]{"onions", "lettuce"}).build();
    }

    @Test
    public void givenAnInstanceOfOrder_whenSerialize_AllValuesAreCorrectInString() throws JsonProcessingException {

        String actual = Order.toJson(stubOrder);
        Assert.assertTrue(actual.contains("15"));
        Assert.assertTrue(actual.contains("onions"));
        Assert.assertTrue(actual.contains("\"category\":\"sandwich\""));


    }

    @Test
    public void givenAJSOn_whenDeserialize_AllValuesAreCorrectInObject() throws IOException {

        Order actual = Order.fromJson(stubOrderAsJson);
        Assert.assertTrue("sandwich".equals(actual.getCategory()));
        Assert.assertTrue("green".equals(actual.getRewardScheme()));



    }
}
