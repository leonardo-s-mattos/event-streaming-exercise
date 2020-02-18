package com.mattos.exercise.movingaverage.service.impl;

import com.mattos.exercise.movingaverage.service.ChatService;
import com.mattos.exercise.domain.SalesOrder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;


@EnableBinding(Source.class)
@Component
public class GitterService implements ChatService<SalesOrder> {

    private final WebClient webClient;

    @Autowired
    public GitterService(WebClient.Builder builder) {
        this.webClient = builder
                            .build();
    }

    @StreamEmitter
    @Output(Source.OUTPUT)
    public Flux<SalesOrder> getSalesOrderStream() {
        return webClient.get()
                        .uri("http://localhost:8080/order-stream")
                        .retrieve()
                        .bodyToFlux(SalesOrder.class)
                        .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500));
    }

    @SneakyThrows
    @StreamEmitter
    @Output(Source.OUTPUT)
    public Flux<SalesOrder> getLatestSalesOrders() {
        return webClient.get()
                        .uri("http://localhost:8080/order-stream")
                        .retrieve()
                        .bodyToFlux(SalesOrder.class)
                        .timeout(Duration.ofSeconds(1))
                        .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500));
    }

}
