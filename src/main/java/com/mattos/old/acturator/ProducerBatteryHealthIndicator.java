package com.mattos.old.acturator;

import com.mattos.old.publisher.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
class ProducerBatteryHealthIndicator implements ReactiveHealthIndicator {


   private final TemperatureSensor newOrdersEventPublisher;

    @Autowired
    public ProducerBatteryHealthIndicator(TemperatureSensor newOrdersEventPublisher) {
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