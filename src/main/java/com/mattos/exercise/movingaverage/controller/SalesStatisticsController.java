package com.mattos.exercise.movingaverage.controller;

import com.mattos.exercise.movingaverage.service.impl.OrderStatisticsService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SalesStatisticsController {
   private final MeterRegistry meterRegistry;
   private final OrderStatisticsService orderStatisticsService;

   // Application monitoring
   private AtomicInteger activeStreams;

   @PostConstruct
   public void init() {
      activeStreams = meterRegistry.gauge("sse.streams", new AtomicInteger(0));
   }

   @GetMapping(path = "/stats-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<String> events() {
      Flux<String> flux = orderStatisticsService.newStatisticsStream()
         .doOnSubscribe(subs -> activeStreams.incrementAndGet())
         .name("stats.sse-stream")
         .metrics()
         .log("sse.stats", Level.FINE)
         .doOnCancel(() -> activeStreams.decrementAndGet())
         .doOnTerminate(() -> activeStreams.decrementAndGet());

      return flux;
   }
}
