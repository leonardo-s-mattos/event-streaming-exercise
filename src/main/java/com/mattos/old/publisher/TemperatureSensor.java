package com.mattos.old.publisher;

import com.mattos.old.domain.Order;
import com.mattos.old.scheduler.MeteredScheduledThreadPoolExecutor;
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
 * Service that probes the current temperature.
 * Also, it reports operational metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureSensor {
   private final MeterRegistry meterRegistry;

   private final Random rnd = new Random();

   private Flux<Order> dataStream;

   @PostConstruct
   public void init() {
      ScheduledExecutorService executor =
         new MeteredScheduledThreadPoolExecutor("temp.sensor", 3, meterRegistry);

      Scheduler eventsScheduler = Schedulers.fromExecutor(executor);
      dataStream = Flux
         .range(0, 10)
         .repeat()
         .concatMap(ignore -> this.probe()
            .delayElement(randomDelay(1000), eventsScheduler)
            .name("temperature.probe")
            .metrics()
            .log("temperature.measurement", Level.FINE))
         .publish()
         .refCount();
      log.info("Temperature Sensor is ready");
   }

   public Flux<Order> temperatureStream() {
      return dataStream;
   }

   public Mono<Integer> batteryLevel() {
      return Mono.fromCallable(() -> {
         int level = rnd.nextInt(100);
         if (level <= 2 ) {
            throw new RuntimeException("Can not connect to the sensor");
         }
         return level;
      });
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

            return newRandomOrder;
         } catch (InterruptedException e) {
            throw new RuntimeException(e);
         } finally {
            log.info("Temperature was measured, took {} milliseconds", delay);
         }
      });
   }
}
