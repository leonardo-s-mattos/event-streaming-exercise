package com.mattos.exercise.repository;

import com.mattos.exercise.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderCrudRepository
            extends ReactiveCrudRepository<Order, String> {

    Flux<Order> findAllByCategory(String category);
    Mono<Order> findFirstByCategory(Mono<String> category);


}
