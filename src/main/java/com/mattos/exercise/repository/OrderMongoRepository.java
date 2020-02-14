package com.mattos.exercise.repository;

import com.mattos.exercise.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderMongoRepository extends ReactiveMongoRepository<Order, String> {

}
