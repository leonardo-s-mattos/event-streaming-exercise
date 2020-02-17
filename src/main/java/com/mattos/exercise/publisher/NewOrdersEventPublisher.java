package com.mattos.exercise.publisher;

import com.mattos.exercise.aggregation.MovingAverageCalculator;
import com.mattos.exercise.aggregation.NewOrdersDatabaseLogger;
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
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;

@Slf4j
@Service
public class NewOrdersEventPublisher extends SubmissionPublisher<String> {

    private static final Logger log = LoggerFactory.getLogger(NewOrdersEventPublisher.class);
    private final Random rnd = new Random();

    private final MeterRegistry meterRegistry;

    private Flux<Order> dataStream;

    @Autowired
    public NewOrdersEventPublisher(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {

        this.subscribe( new NewOrdersDatabaseLogger());
        this.subscribe( new MovingAverageCalculator());

        ScheduledExecutorService executor =
                new MeteredScheduledThreadPoolExecutor("order", 3, meterRegistry);

        Scheduler eventsScheduler = Schedulers.fromExecutor(executor);
        dataStream = Flux
                .range(0, 10)
                .repeat()
                .concatMap(ignore -> this.probe()
                        .delayElement(randomDelay(1000), eventsScheduler)
                        .name("order.publisher")
                        .metrics()
                        .log("order.publisher.log", Level.FINE))
                .publish()
                .refCount();
        log.info("New Order Publisher is ready");
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
                Order newRandomOrder = new Order.Builder().withId(String.valueOf(100000 * rnd.nextGaussian())).withCategory("sandwich")
                        .withSale("turkey sub", rnd.nextGaussian() * 1000, "green", null, new String[]{"onions", "lettuce"}).build();
                this.submit(Order.toJson(newRandomOrder));
                return newRandomOrder;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                log.info("New order was generated, took {} milliseconds", delay);
            }
        });
    }

    public Mono<Integer> batteryLevel() {
        return Mono.fromCallable(() -> {
            int level = rnd.nextInt(100);
            if (level <= 2 ) {
                throw new RuntimeException("Can not connect to the publisher");
            }
            return level;
        });
    }

}
