package com.mattos.exercise.movingaverage.repository;

import com.mattos.exercise.domain.SalesOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends ReactiveMongoRepository<SalesOrder, String> {
}
