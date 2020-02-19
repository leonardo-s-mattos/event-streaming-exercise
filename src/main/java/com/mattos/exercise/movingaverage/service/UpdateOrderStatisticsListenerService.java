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
 *
 * For the cache, here is one of the tade offs...  that for production scale it would not work obviously.
 * For real life and thinking on having multiple instances of this service, a distributed cache service like redis
 * would be useful.
 */
@Slf4j
public abstract class UpdateOrderStatisticsListenerService implements Subscriber<String> {

    @Autowired protected AggregationConfig aggregationConfig;

    protected Subscription subscription;

    protected List<SalesOrder> last3CachedOrders;

    protected Flux<SalesStatisticVM> dataStream;

    public abstract String whichCategoryAmI();

    @Override
    public void onNext(String json) {
        try {
            SalesOrder newNotifiedOrder = SalesOrder.fromJson(json);

            // grab the current list from cache for given category

            if (last3CachedOrders == null) {
                last3CachedOrders = new ArrayList<>();
            }

            if (last3CachedOrders.size() == aggregationConfig.getMaximumSize()) {
                last3CachedOrders.remove(0);
            }

            last3CachedOrders.add(newNotifiedOrder);


            // now doing the calculation
            Double averagePrice = Precision.round(last3CachedOrders
                    .stream()
                    .mapToDouble(v -> v.getPrice())
                    .average().orElse(0D),2);

            Double averageRewards = Precision.round(last3CachedOrders
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
