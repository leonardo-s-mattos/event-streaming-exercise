package com.mattos.old.aggregation;


import com.mattos.old.config.AggregationConfig;
import com.mattos.old.config.RewardSchemaConfig;
import com.mattos.old.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;

@Slf4j
@Component
public class MovingAverageCalculator implements Flow.Subscriber<String> {

    @Autowired private AggregationConfig aggregationConfig;
    @Autowired private RewardSchemaConfig rewardSchemeConfig;

    private Map<String, List<Order>> cacheForTheOrders;

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.cacheForTheOrders = new HashMap<>();
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("Moving Average Calculator - Received item: " + item);

        try {
            Order newNotifiedOrder = Order.fromJson(item);

            List<Order> last3ChachedOrders = this.cacheForTheOrders.get(newNotifiedOrder.getCategory());
            if (last3ChachedOrders == null) {
                last3ChachedOrders = new ArrayList<>();
            }

            if (last3ChachedOrders.size() == 3) {
                last3ChachedOrders.remove(0);
            }

            last3ChachedOrders.add(newNotifiedOrder);

            System.out.println("New Order Publisher is ready");

           // this.submit(newNotifiedOrder);


        } catch ( IOException e){
            System.out.println("Problem to parse event data. For now I will ignore this record.");
        } finally {
            subscription.request(1);
        }


    }
    @Override
    public void onError(Throwable error) {
        System.out.println("Error occurred: " + error.getMessage());
    }
    @Override
    public void onComplete() {
        System.out.println("PrintSubscriber is complete");
    }

}
