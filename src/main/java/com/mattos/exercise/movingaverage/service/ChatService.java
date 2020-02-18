package com.mattos.exercise.movingaverage.service;

import reactor.core.publisher.Flux;

public interface ChatService<T> {

	Flux<T> getSalesOrderStream();

	Flux<T> getLatestSalesOrders();
}
