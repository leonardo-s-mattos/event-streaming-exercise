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
    private UpdateDrinkOrderStatisticsListenerService target;

    @Mock
    private AggregationConfig mockAggregationConfig;

    @Mock
    private Subscription mockSubscription;

    private List<SalesOrder> cacheForTheOrders = new ArrayList<>();

    @Before public void init(){

        ReflectionTestUtils.setField(target, "last3CachedOrders", cacheForTheOrders);
        ReflectionTestUtils.setField(target, "subscription", mockSubscription);

    }


    @Test
    public void onNext_given5Orders_whenSameCategory_thenCalculateTheStatsForLast3AndFirst2AreNotPresent(){

        //Start With Clean Cache
        cacheForTheOrders.clear();
        when(mockAggregationConfig.getMaximumSize()).thenReturn(3);

        String testCategory = "drink";
        List<SalesOrder> givenSalesOrders =
                StubDataRepository.getSalesOrderInstancesDifferentPrice(testCategory, 1D, 2D, 3D, 4D, 5D);

        //Run the target method for all instances
        givenSalesOrders.stream().map(v->SalesOrder.toJson(v)).forEach(target::onNext);

        //Validate the results
        Assert.assertThat(cacheForTheOrders.size(), Matchers.is(3)); // Juts the limit
        Assert.assertThat(cacheForTheOrders.stream()
                .findFirst().get().getPrice(), Matchers.is(3.0D)); // The first on the list is the 3rd element

    }




}
