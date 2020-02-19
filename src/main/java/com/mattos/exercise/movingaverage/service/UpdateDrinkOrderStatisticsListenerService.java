package com.mattos.exercise.movingaverage.service;


import com.mattos.exercise.domain.SalesOrder;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;

import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class UpdateDrinkOrderStatisticsListenerService extends UpdateOrderStatisticsListenerService implements Subscriber<String> {

    public  String whichCategoryAmI(){
        return "drink";
    };





}
