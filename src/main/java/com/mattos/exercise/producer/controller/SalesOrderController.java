package com.mattos.exercise.producer.controller;

import com.mattos.exercise.producer.service.SalesOrderProvider;
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
public class SalesOrderController {
   private final MeterRegistry meterRegistry;
   private final SalesOrderProvider salesOrderProvider;

   // Application monitoring
   private AtomicInteger activeStreams;

   @PostConstruct
   public void init() {
      activeStreams = meterRegistry.gauge("sse.streams", new AtomicInteger(0));
   }

   @GetMapping(path = "/order-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<String> events() {
      return salesOrderProvider.temperatureStream()
         .doOnSubscribe(subs -> activeStreams.incrementAndGet())
         .name("order.sse-stream")
         .metrics()
         .log("sse.order", Level.FINE)
         .doOnCancel(() -> activeStreams.decrementAndGet())
         .doOnTerminate(() -> activeStreams.decrementAndGet());
   }
}
