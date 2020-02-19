package com.mattos.exercise.movingaverage.service;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.movingaverage.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * This is the listener for the same new sales order queue
 * I just created this on to store on Mongo, to demonstrate that on real scenario, would be necessary
 * to store the events and be able to replay them later, in case the Statistics service is down
 * ( even with the cached reading )
 */
@Slf4j
@Component
public class NewSalesOrderListenerService implements Subscriber<String> {


    @Autowired
    private SalesRepository salesRepository;
    private Subscription subscription;

    @Override
    public void onNext(String newOrder) {
        // store in in memory MongoDB
        try {
            final SalesOrder arrivingOrder = SalesOrder.fromJson(newOrder);

            if(isValidRecord(arrivingOrder)){
                salesRepository.save(arrivingOrder);
            }

        } catch (IOException e) {
            log.error("Received an invalid Order and did not persisted on db");
        } finally {
            subscription.request(1);
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
