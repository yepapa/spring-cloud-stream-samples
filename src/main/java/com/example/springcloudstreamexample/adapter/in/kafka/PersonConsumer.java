package com.example.springcloudstreamexample.adapter.in.kafka;

import com.example.springcloudstreamexample.adapter.out.kafka.dto.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Component("personConsumer")
public class PersonConsumer implements Consumer<Flux<Person>> {
    @Override
    public void accept(Flux<Person> personFlux) {
        log.info("personFlux : {}", personFlux);
        personFlux.doOnNext(person -> System.out.println("Person Received : " + person.getName()))
                .subscribe();
    }
}
