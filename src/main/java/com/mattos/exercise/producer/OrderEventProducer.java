package com.mattos.exercise.producer;

import com.mattos.exercise.domain.Order;
import com.mattos.exercise.scheduler.MeteredScheduledThreadPoolExecutor;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

@Slf4j
@Service
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private final Random rnd = new Random();

    private final MeterRegistry meterRegistry;

    /*
    private final Observable<String> dataStream =
            Observable
                    .range(0, Integer.MAX_VALUE)
                    .concatMap(ignore -> Observable
                            .just(1)
                            .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS)
                            .map(ignore2 -> this.probe()))
                    .publish()
                    .refCount();

    public Observable<String> orderStream() {
        return dataStream;
    }

    private String probe()  {
        Order generatedOrder = new Order.Builder().withId(String.valueOf(100000 + rnd.nextGaussian())).withCategory("sandwich")
                .withSale("turkey sub", 16 + rnd.nextGaussian() * 10, "green", null, new String[]{"onions", "lettuce"}).build();

        return Order.toJson(generatedOrder);
    }*/

    private Flux<Order> dataStream;

    @Autowired
    public OrderEventProducer(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        ScheduledExecutorService executor =
                new MeteredScheduledThreadPoolExecutor("order", 3, meterRegistry);

        Scheduler eventsScheduler = Schedulers.fromExecutor(executor);
        dataStream = Flux
                .range(0, 10)
                .repeat()
                .concatMap(ignore -> this.probe()
                        .delayElement(randomDelay(1000), eventsScheduler)
                        .name("order.producer")
                        .metrics()
                        .log("moving.average", Level.FINE))
                .publish()
                .refCount();
        log.info("Order Producer is ready");
    }

    public Flux<Order> orderStream() {
        return dataStream;
    }

    // --- Supporting methods

    private Duration randomDelay(int maxMillis) {
        return Duration.ofMillis(rnd.nextInt(maxMillis));
    }

    private Mono<Order> probe() {
        return Mono.fromCallable(() -> {
            long delay = randomDelay(100).toMillis();
            try {
                Thread.sleep(delay);
                return new Order.Builder().withId(String.valueOf(100000 + rnd.nextGaussian())).withCategory("sandwich")
                        .withSale("turkey sub", 16 + rnd.nextGaussian() * 10, "green", null, new String[]{"onions", "lettuce"}).build();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                log.info("Order was generated, took {} milliseconds", delay);
            }
        });
    }

    public Mono<Integer> batteryLevel() {
        return Mono.fromCallable(() -> {
            int level = rnd.nextInt(100);
            if (level <= 2 ) {
                throw new RuntimeException("Can not connect to the producer");
            }
            return level;
        });
    }

}
