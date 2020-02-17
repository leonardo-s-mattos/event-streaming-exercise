package com.mattos.exercise.acturator;

import com.mattos.exercise.producer.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
class ProducerBatteryHealthIndicator implements ReactiveHealthIndicator {


   private final OrderEventProducer orderEventProducer;

    @Autowired
    public ProducerBatteryHealthIndicator(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @Override
   public Mono<Health> health() {
      return orderEventProducer
         .batteryLevel()
         .map(level -> {
            if (level > 40) {
               return new Health.Builder()
                  .up()
                  .withDetail("level", level)
                  .build();
            } else {
               return new Health.Builder()
                  .status(new Status("Low Battery"))
                  .withDetail("level", level)
                  .build();
            }
         }).onErrorResume(err -> Mono.
            just(new Health.Builder()
               .outOfService()
               .withDetail("error", err.getMessage())
               .build())
         );
   }
}