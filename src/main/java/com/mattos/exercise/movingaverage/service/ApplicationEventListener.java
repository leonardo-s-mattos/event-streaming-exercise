package com.mattos.exercise.movingaverage.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/*
 * I am using this class as listener that all being were loaded
 * and then I can subscribe to the queue.
 * This is needed because our generator is in the same context
 */
@Component
public class ApplicationEventListener {

    @Autowired
    private NewSalesOrderService newSalesOrderService;
    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event){
        final WebClient webClient = WebClient.create("http://localhost:8080/");
        webClient.get().uri("/order-stream").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
                .bodyToFlux(String.class).log()
                .subscribe(newSalesOrderService);

        webClient.get().uri("/order-stream").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
                .bodyToFlux(String.class).log()
                .subscribe(orderStatisticsService);
    }
}
