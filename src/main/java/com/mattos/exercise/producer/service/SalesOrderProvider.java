package com.mattos.exercise.producer.service;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.producer.scheduler.MeteredScheduledThreadPoolExecutor;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Service that generates random Sales
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderProvider {
   private final MeterRegistry meterRegistry;

   private final Random rnd = new Random();

   private Flux<String> dataStream;

   @PostConstruct
   public void init() {
      ScheduledExecutorService executor =
         new MeteredScheduledThreadPoolExecutor("newOrder.probe", 3, meterRegistry);

      Scheduler eventsScheduler = Schedulers.fromExecutor(executor);
      dataStream = Flux
         .range(0, 10)
         .repeat()
         .concatMap(ignore -> this.probe()
            .delayElement(randomDelay(1000), eventsScheduler)
            .name("newOrder.probe")
            .metrics()
            .log("salesOrder.generator", Level.FINE))
         .publish()
         .refCount();
      log.info("Sales Order Generator is ready");
   }

   public Flux<String> newSalesOrderStream() {
      return dataStream;
   }

   // --- Supporting methods

   private Duration randomDelay(int maxMillis) {
      return Duration.ofMillis(rnd.nextInt(maxMillis));
   }

   private Mono<String> probe() {
      return Mono.fromCallable(() -> {
         long delay = randomDelay(100).toMillis();
         try {
            Thread.sleep(delay);
            return SalesOrder.toJson(RandomSalesOrderGenerator.getNewRandomSalesOrder());
         } catch (InterruptedException e) {
            throw new RuntimeException(e);
         } finally {
            log.info("New sales order generated, took {} milliseconds", delay);
         }
      });
   }
}
