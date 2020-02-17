package com.mattos.exercise.aggregation;

import com.mattos.exercise.config.AggregationConfig;

import com.mattos.exercise.config.RewardSchemaConfig;
import com.mattos.exercise.publisher.NewOrdersEventPublisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;

@Component
public class MovingAverage<T> implements Subscriber<T> {

    @Autowired private AggregationConfig aggregationConfig;
    @Autowired private RewardSchemaConfig rewardSchemeConfig;
    @Autowired private NewOrdersEventPublisher newOrdersEventPublisher;

    private Subscription subscription;

    @PostConstruct
    public void subscribe(){
        newOrdersEventPublisher.orderStream().subscribe();
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T t) {
        System.out.println("Got : " + t);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Done");
    }

}
