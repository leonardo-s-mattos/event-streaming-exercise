package com.mattos.exercise.movingaverage.service.impl;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.domain.vm.SalesStatisticVM;
import com.mattos.exercise.movingaverage.repository.SalesRepository;
import com.mattos.exercise.movingaverage.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;



@EnableBinding(Processor.class)
@Component
public class DefaultStatisticService implements StatisticService {


    private final SalesRepository salesRepository;

    @Autowired
    public DefaultStatisticService(
            SalesRepository messageRepository) {
        this.salesRepository = messageRepository;
    }

    @Override
    @StreamListener(Processor.INPUT)
    @Output(Processor.OUTPUT)
    public Flux<SalesStatisticVM> updateStatistic(Flux<SalesOrder> messagesFlux) {
        return messagesFlux.transform(salesRepository::saveAll)
                           .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500))
                           .onBackpressureLatest()
                           .concatMap(e -> this.doGetSalesOrderStatistics(), 1)
                           .onErrorContinue((t, e) -> {});
    }

    private Mono<SalesStatisticVM> doGetSalesOrderStatistics() {

        /* here must be the logic to retrieve the statistics from Cache or Cassandra*/

        return null;
    }

    /*
    public static void main(String[] args) {
        String[] newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[args.length] = "--spring.profiles.active=statistic";

        SpringApplication.run(DefaultStatisticService.class, args);
    } */
}