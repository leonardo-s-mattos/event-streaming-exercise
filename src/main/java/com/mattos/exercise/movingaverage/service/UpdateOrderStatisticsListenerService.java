package com.mattos.exercise.movingaverage.service;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.domain.vm.SalesStatisticVM;
import com.mattos.exercise.movingaverage.config.AggregationConfig;
import com.mattos.exercise.movingaverage.config.RewardSchemaConfig;
import com.mattos.exercise.movingaverage.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This is a listener for the new orders and do a simple calculation of the moving average
 * for the configured maximum number of records per category
 */
@Slf4j
@Component
public class UpdateOrderStatisticsListenerService implements Subscriber<String> {

    @Autowired private AggregationConfig aggregationConfig;

    private Subscription subscription;
    private Map<String, List<SalesOrder>> cacheForTheOrders;

    private Flux<SalesStatisticVM> dataStream;

    @Override
    public void onNext(String json) {
        try {
            SalesOrder newNotifiedOrder = SalesOrder.fromJson(json);

            // grab the current list from cache for given category
            List<SalesOrder> last3ChachedOrders = this.cacheForTheOrders.get(newNotifiedOrder.getCategory());
            if (last3ChachedOrders == null) {
                last3ChachedOrders = new ArrayList<>();
            }

            if (last3ChachedOrders.size() == aggregationConfig.getMaximumSize()) {
                last3ChachedOrders.remove(0);
            }

            last3ChachedOrders.add(newNotifiedOrder);

            // Return the modified list to cache
            this.cacheForTheOrders.put(newNotifiedOrder.getCategory(),last3ChachedOrders);


            // now doing the calculation
            Double averagePrice = Precision.round(last3ChachedOrders
                    .stream()
                    .mapToDouble(v -> v.getPrice())
                    .average().orElse(0D),2);

            Double averageRewards = Precision.round(last3ChachedOrders
                    .stream()
                    .mapToDouble(v -> v.getPoints())
                    .average().orElse(0D),2);

            dataStream = Flux.just(new SalesStatisticVM(newNotifiedOrder.getCategory(), averagePrice, averageRewards));

        } catch ( IOException e){
            log.error("Problem to parse event data. For now I will ignore this record.");
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
        cacheForTheOrders = new HashMap<>();
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

    public Flux<SalesStatisticVM> newStatisticsStream() {
        return dataStream;
    }

}
