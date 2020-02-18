package com.mattos.exercise;

import com.mattos.old.scheduler.MeteredSchedulersFactory;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@EnableAdminServer
@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = {
		"com.mattos.exercise.movingaverage.repository",
		"com.mattos.exercise.movingaverage.repository.impl"
})
public class ExerciseApplication  {

	private final MeterRegistry meterRegistry;

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Hooks.onNextDropped(c -> meterRegistry.counter("reactor.dropped.events").increment());
		Schedulers.setFactory(new MeteredSchedulersFactory(meterRegistry));
		log.info("Updated Scheduler factory with a custom instance");
	}


}

