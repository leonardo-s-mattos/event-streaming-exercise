package com.mattos.exercise.movingaverage.service.impl;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.movingaverage.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class NewSalesOrderService implements Subscriber<String> {

    private Subscription subscription;
    @Autowired
    private SalesRepository salesRepository;

    @Override
    public void onNext(String t) {
        // store in in memory MongoDB
        try {
            final SalesOrder arrivingOrder = SalesOrder.fromJson(t);

            if(isValidRecord(arrivingOrder)){
                salesRepository.save(arrivingOrder);
            }

        } catch (IOException e) {
            log.error("Received an invalid Order and did not persisted on db");
        }

    }

    private boolean isValidRecord(SalesOrder arrivingOrder) {
        return arrivingOrder!=null && arrivingOrder.getId() !=null;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }
    @Override
    public void onError(Throwable t) {
        log.error("Error when consuming  New Sales Stream : " + t.getMessage());
    }
    @Override
    public void onComplete() {
        log.info("New order just stored");
    }

}
