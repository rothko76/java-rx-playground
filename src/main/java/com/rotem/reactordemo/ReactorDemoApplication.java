package com.rotem.reactordemo;

import java.time.Duration;
import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;
import reactor.tools.agent.ReactorDebugAgent;

@Log4j2
@SpringBootApplication

public class ReactorDemoApplication {

	public void error(Throwable t) {
		log.info("oh shit");
		log.info("Thread:"+Thread.currentThread().getName());
		log.error(t);
	}
	public static void main(String[] args) {
		//uncomment for reactive streams debugging
		//Hooks.onOperatorDebug(); 
		ReactorDebugAgent.init();
		ReactorDebugAgent.processExistingClasses();
		SpringApplication.run(ReactorDemoApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void go() {
//		System.out.println("application started");
//		Flux<String> letters = Flux.just("A", "B", "C", "D", "E", "F")
//				.map(letter -> letter.toLowerCase())
//				.subscribeOn(Schedulers.immediate())
//				.log().publishOn(Schedulers.immediate())
//				.doOnError(this::error);
//		//.checkpoint(" ##################################see errors here",true ) ;
//		//Consumer<String> consumer = letter -> log.info(letter);
//				
//		letters.delaySubscription(Duration.ofSeconds(5)).subscribe(log::info);
//		
//	}
	
	
	@EventListener(ApplicationReadyEvent.class)
	public void sink() {
		System.out.println("application started sink test");
		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
		    while(true) {
		        fluxSink.next(System.currentTimeMillis());
		    }
		})
		  .publish();
		
		
		//.checkpoint(" ##################################see errors here",true ) ;
		//Consumer<String> consumer = letter -> log.info(letter);
				
		publish.sample(Duration.ofSeconds(3)).subscribe(log::info);
		publish.sample(Duration.ofSeconds(1)).subscribe(log::warn);
		publish.connect();
		
	}

}
