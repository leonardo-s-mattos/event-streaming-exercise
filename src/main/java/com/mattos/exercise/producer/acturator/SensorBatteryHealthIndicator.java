package com.mattos.exercise.producer.acturator;

import com.mattos.exercise.producer.service.SalesOrderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
class SensorBatteryHealthIndicator implements ReactiveHealthIndicator {
   private final SalesOrderProvider salesOrderProvider;

   @Override
   public Mono<Health> health() {
      return salesOrderProvider
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