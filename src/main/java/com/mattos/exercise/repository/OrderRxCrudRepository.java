package com.mattos.exercise.repository;

import com.mattos.exercise.domain.Order;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;

public interface OrderRxCrudRepository extends RxJava2CrudRepository<Order, String> {

    public Observable<Order> findAllByCategory(String category);
    public Single<Order> findFirstByCategory(Single<String> category);
}
