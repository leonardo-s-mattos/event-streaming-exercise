package com.mattos.exercise.producer.service;

import com.mattos.exercise.domain.SalesOrder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class RandomSalesOrderGeneratorTest {

    @Test
    public void getNewRandomSalesOrder__whenRequested2times_thenReturn2InstancesWithDifferentValues(){

        SalesOrder firstActual = RandomSalesOrderGenerator.getNewRandomSalesOrder();
        SalesOrder secondActual = RandomSalesOrderGenerator.getNewRandomSalesOrder();

        Assert.assertThat(firstActual, not(sameInstance(secondActual)));
        Assert.assertThat(firstActual.getPrice().compareTo(secondActual.getPrice()), not(is(0)));

    }
}
