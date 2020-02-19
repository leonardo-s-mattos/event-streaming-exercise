package com.mattos.exercise.movingaverage.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.mattos.exercise.domain.RewardScheme;
import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.movingaverage.config.AggregationConfig;
import com.mattos.exercise.utils.StubDataRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Subscription;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;


@RunWith(MockitoJUnitRunner.class)
public class UpdateOrderStatisticsListenerServiceTest {

    @InjectMocks
    private UpdateOrderStatisticsListenerService target;

    @Mock
    private AggregationConfig mockAggregationConfig;

    @Mock
    private Subscription mockSubscription;

    private Map<String, List<SalesOrder>> cacheForTheOrders = new HashMap<>();

    @Before public void init(){

        ReflectionTestUtils.setField(target, "cacheForTheOrders", cacheForTheOrders);
        ReflectionTestUtils.setField(target, "subscription", mockSubscription);

    }


    @Test
    public void onNext_given3Orders_whenDifferentCategories_thenCachedTheMapContain3Records(){

        //Start With Clean Cache
        cacheForTheOrders.clear();

        when(mockAggregationConfig.getMaximumSize()).thenReturn(3);

        String testCategory = "retail";
        List<SalesOrder> givenSalesOrders =
                Arrays.asList(StubDataRepository.getRandomSalesOrderInstance("retail"),
                StubDataRepository.getRandomSalesOrderInstance("drink"),
                StubDataRepository.getRandomSalesOrderInstance("sandwich"));

        //Run the target method for all instances
        givenSalesOrders.stream().map(v->SalesOrder.toJson(v)).forEach(target::onNext);

        //Validate the results
        Assert.assertThat(cacheForTheOrders.size(), Matchers.is(3));

    }


    @Test
    public void onNext_given2Orders_whenSameCategory_thenTheCachedMapContain1Records(){

        //Start With Clean Cache
        cacheForTheOrders.clear();
        when(mockAggregationConfig.getMaximumSize()).thenReturn(3);

        String testCategory = "retail";
        List<SalesOrder> givenSalesOrders = StubDataRepository.getSalesOrderInstancesDifferentPrice(testCategory,2D,5D);

        //Run the target method for all instances
        givenSalesOrders.stream().map(v->SalesOrder.toJson(v)).forEach(target::onNext);

        //Validate the results
        Assert.assertThat(cacheForTheOrders.size(), Matchers.is(1));

    }

    @Test
    public void onNext_given5Orders_whenSameCategory_thenCalculateTheStatsForLast3AndFirst2AreNotPresent(){

        //Start With Clean Cache
        cacheForTheOrders.clear();
        when(mockAggregationConfig.getMaximumSize()).thenReturn(3);

        String testCategory = "retail";
        List<SalesOrder> givenSalesOrders =
                StubDataRepository.getSalesOrderInstancesDifferentPrice(testCategory, 1D, 2D, 3D, 4D, 5D);

        //Run the target method for all instances
        givenSalesOrders.stream().map(v->SalesOrder.toJson(v)).forEach(target::onNext);

        //Validate the results
        Assert.assertThat(cacheForTheOrders.size(), Matchers.is(1));  // Just One Category
        Assert.assertThat(cacheForTheOrders.get(testCategory).size(), Matchers.is(3)); // Juts the limit
        Assert.assertThat(cacheForTheOrders.get(testCategory).stream()
                .findFirst().get().getPrice(), Matchers.is(3.0D)); // The first on the list is the 3rd element





    }




}
