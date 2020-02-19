package com.mattos.exercise.movingaverage.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/*
 * I am using this class as listener that all being were loaded
 * and then I can subscribe to the queue.
 * This is needed because our generator is in the same context
 */
@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "orderstream")
public class ApplicationEventListener {

    private String url;
    @Autowired
    private UpdateDrinkOrderStatisticsListenerService updateDrinkOrderStatisticsListenerService;
    @Autowired
    private UpdateRetailOrderStatisticsListenerService updateRetailOrderStatisticsListenerService;
    @Autowired
    private UpdateSandwichOrderStatisticsListenerService updateSandwichOrderStatisticsListenerService;
    @Autowired
    private NewSalesOrderListenerService newSalesOrderListenerService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event){
        final WebClient webClient = WebClient.create(url);
        webClient.get().uri("/order-stream").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
                .bodyToFlux(String.class).log()
                .subscribe(newSalesOrderListenerService);

        webClient.get().uri("/order-stream").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
                .bodyToFlux(String.class).log()
                .filter(v->v.contains("drink"))
                .subscribe(updateDrinkOrderStatisticsListenerService);

        webClient.get().uri("/order-stream").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
                .bodyToFlux(String.class).log()
                .filter(v->v.contains("retail"))
                .subscribe(updateRetailOrderStatisticsListenerService);

        webClient.get().uri("/order-stream").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
                .bodyToFlux(String.class).log()
                .filter(v->v.contains("sandwich"))
                .subscribe(updateSandwichOrderStatisticsListenerService);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
