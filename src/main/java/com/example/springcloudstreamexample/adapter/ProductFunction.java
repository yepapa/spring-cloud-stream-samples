package com.example.springcloudstreamexample.adapter;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component("product")
public class ProductFunction implements Function<Flux<String>, Mono<Void>> {
    @Override
    public Mono<Void> apply(Flux<String> flux) {
        return flux.doOnNext(s -> System.out.println("Product Received : " + s.toUpperCase())).then();
    }
}
