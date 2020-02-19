package com.mattos.exercise.movingaverage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.movingaverage.repository.SalesRepository;
import com.mattos.exercise.utils.StubDataRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Subscription;



@RunWith(MockitoJUnitRunner.class)
public class NewSalesOrderListenerServiceTest {
    @InjectMocks
    private NewSalesOrderListenerService target;

    @Mock
    private SalesRepository salesRepository;
    @Mock
    private Subscription subscription;

    @Before public void init(){

    }

    @Test
    public void onNext_givenANewSalesOrderInJSON_whenReceived_persistTheNewOrderInTheDatabase(){

        target.onNext(SalesOrder.toJson(StubDataRepository.getRandomSalesOrderInstance("retail")));

        verify(salesRepository,times(1)).save(any(SalesOrder.class));
        verify(subscription,times(1) ).request(1);
    }
}
