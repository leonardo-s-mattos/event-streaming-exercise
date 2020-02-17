package com.mattos.exercise.controller;

import com.mattos.exercise.domain.Order;
import com.mattos.exercise.producer.OrderEventProducer;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

@Slf4j
@RestController
public class EventStreamController {
   private final MeterRegistry meterRegistry;
   private final OrderEventProducer orderEventProducer;

   // Application monitoring
   private AtomicInteger activeStreams;

   @Autowired
   public EventStreamController(MeterRegistry meterRegistry, OrderEventProducer orderEventProducer) {
      this.meterRegistry = meterRegistry;
      this.orderEventProducer = orderEventProducer;
   }

   @PostConstruct
   public void init() {
      activeStreams = meterRegistry.gauge("sse.streams", new AtomicInteger(0));
   }

   @GetMapping(path = "/order-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<Order> events() {
      return orderEventProducer.orderStream()
         .doOnSubscribe(subs -> activeStreams.incrementAndGet())
         .name("order.sse-stream")
         .metrics()
         .log("sse.order", Level.FINE)
         .doOnCancel(() -> activeStreams.decrementAndGet())
         .doOnTerminate(() -> activeStreams.decrementAndGet());
   }
/*
   @GetMapping(path = "/average-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<Average> events() {
      return orderEventProducer.orderStream()
              .doOnSubscribe(subs -> activeStreams.incrementAndGet())
              .name("average.sse-stream")
              .metrics()
              .log("sse.average", Level.FINE)
              .doOnCancel(() -> activeStreams.decrementAndGet())
              .doOnTerminate(() -> activeStreams.decrementAndGet());
   }

 */
}
