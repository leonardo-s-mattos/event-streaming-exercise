package com.mattos.exercise.movingaverage.controller;

import com.mattos.exercise.domain.vm.SalesStatisticVM;
import com.mattos.exercise.movingaverage.service.UpdateDrinkOrderStatisticsListenerService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

@Slf4j
@RestController
public class DrinkSalesStatisticsController {
   private MeterRegistry meterRegistry;
   private UpdateDrinkOrderStatisticsListenerService updateDrinkOrderStatisticsListenerService;

   // Application monitoring
   private AtomicInteger activeStreams;

   public DrinkSalesStatisticsController(MeterRegistry meterRegistry, UpdateDrinkOrderStatisticsListenerService updateDrinkOrderStatisticsListenerService) {
      this.meterRegistry = meterRegistry;
      this.updateDrinkOrderStatisticsListenerService = updateDrinkOrderStatisticsListenerService;
   }

   @PostConstruct
   public void init() {
      activeStreams = meterRegistry.gauge("sse.streams", new AtomicInteger(0));
   }

   @GetMapping(path = "/drink-stats-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<SalesStatisticVM> events() {
      Flux<SalesStatisticVM> flux = updateDrinkOrderStatisticsListenerService.newStatisticsStream()
         .doOnSubscribe(subs -> activeStreams.incrementAndGet())
         .name("stats.sse-stream")
         .metrics()
         .log("sse.stats", Level.FINE)
         .doOnCancel(() -> activeStreams.decrementAndGet())
         .doOnTerminate(() -> activeStreams.decrementAndGet());

      return flux;
   }
}
