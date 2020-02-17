package com.mattos.exercise;

import com.mattos.exercise.scheduler.MeteredSchedulersFactory;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@EnableAdminServer
@SpringBootApplication
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

	//public static void main(String[] args) {
	//	SpringApplication.run(ExerciseApplication.class, args);
	//}



}

