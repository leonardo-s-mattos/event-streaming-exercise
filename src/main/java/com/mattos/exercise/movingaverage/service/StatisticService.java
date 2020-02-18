package com.mattos.exercise.movingaverage.service;

import com.mattos.exercise.domain.SalesOrder;
import com.mattos.exercise.domain.vm.SalesStatisticVM;
import reactor.core.publisher.Flux;

public interface StatisticService {

	Flux<SalesStatisticVM> updateStatistic(Flux<SalesOrder> sales);
}
