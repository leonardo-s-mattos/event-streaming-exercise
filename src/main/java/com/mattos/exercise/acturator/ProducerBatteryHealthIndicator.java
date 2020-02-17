package com.mattos.exercise.acturator;

import com.mattos.exercise.publisher.NewOrdersEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
class ProducerBatteryHealthIndicator implements ReactiveHealthIndicator {


   private final NewOrdersEventPublisher newOrdersEventPublisher;

    @Autowired
    public ProducerBatteryHealthIndicator(NewOrdersEventPublisher newOrdersEventPublisher) {
        this.newOrdersEventPublisher = newOrdersEventPublisher;
    }

    @Override
   public Mono<Health> health() {
      return newOrdersEventPublisher
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