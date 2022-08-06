package com.example.springcloudstreamexample.adapter.out.kafka;

import com.example.springcloudstreamexample.adapter.out.kafka.dto.Person;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component("personSupplier")
public class PersonSupplier implements Supplier<Flux<Person>> {

    private final AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public Flux<Person> get() {
        return Flux.fromStream(Stream.generate(() -> {
            try {
                Thread.sleep(1000);
                int i = atomicInteger.incrementAndGet();
                System.out.println("Person Send i - " + i);
                return new Person("Tom - " + i);
            } catch (Exception e) {
                // ignore
            }
            return null;
        })).subscribeOn(Schedulers.boundedElastic()).share();
    }
}
