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

/*
 * Exposes via web service ( Reactive API ) the stream of events ( new orders )
 * Simple usage of SSE to push the changes to UI..  and with the listener on that side, keep the communication open
 * and with backpressure
 */
@Slf4j
@RestController
public class SalesOrderController {
   private MeterRegistry meterRegistry;
   private SalesOrderProvider salesOrderProvider;

   // Application monitoring
   private AtomicInteger activeStreams;

   public SalesOrderController(MeterRegistry meterRegistry, SalesOrderProvider salesOrderProvider) {
      this.meterRegistry = meterRegistry;
      this.salesOrderProvider = salesOrderProvider;
   }

   @PostConstruct
   public void init() {
      activeStreams = meterRegistry.gauge("sse.streams", new AtomicInteger(0));
   }

   @GetMapping(path = "/order-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<String> events() {
      Flux<String> flux = salesOrderProvider.newSalesOrderStream()
         .doOnSubscribe(subs -> activeStreams.incrementAndGet())
         .name("order.sse-stream")
         .metrics()
         .log("sse.order", Level.FINE)
         .doOnCancel(() -> activeStreams.decrementAndGet())
         .doOnTerminate(() -> activeStreams.decrementAndGet());

      return flux;
   }
}
