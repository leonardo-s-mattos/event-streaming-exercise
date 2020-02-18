package com.mattos.exercise.domain;

import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.io.IOException;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class SandwichSalesOrderTest {

    private SalesOrder stubOrder;
    private String stubOrderAsJson = "{\"id\":\"124\",\"category\":\"sandwich\",\"price\":15.0,\"name\":\"turkey sub\",\"addons\":[\"onions\",\"lettuce\"],\"rewards_scheme\":\"GREEN\"}";

    @Before
    public void init(){
        stubOrder = new SandwichSalesOrder.Builder().withId("124").withCategory("sandwich")
                .withSale("turkey sub", 15D, RewardScheme.GREEN, null, Arrays.asList("onions", "lettuce")).build();
    }

    @Test
    public void givenAnInstanceOfOrder_whenSerialize_AllValuesAreCorrectInString() throws JsonProcessingException {

        String actual = DrinkSalesOrder.toJson(stubOrder);
        Assert.assertTrue(actual.contains("15"));
        Assert.assertTrue(actual.contains("GREEN"));
        Assert.assertTrue(actual.contains("onions"));
        Assert.assertTrue(!actual.contains("\"size\":\"large\""));
        
    }

    @Test
    public void givenAJSOn_whenDeserialize_AllValuesAreCorrectInObject() throws IOException {

        SandwichSalesOrder actual = SandwichSalesOrder.fromJson(stubOrderAsJson);
        Assert.assertTrue("sandwich".equals(actual.getCategory()));
        Assert.assertThat(actual.getAddons().size(), is(2));

    }
}
