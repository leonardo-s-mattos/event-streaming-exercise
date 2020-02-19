package com.mattos.exercise.movingaverage.service;


import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class UpdateRetailOrderStatisticsListenerService extends UpdateOrderStatisticsListenerService implements Subscriber<String> {

    public  String whichCategoryAmI(){
        return "retail";
    };


}
