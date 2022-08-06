package com.example.springcloudstreamexample;

import com.example.springcloudstreamexample.adapter.in.kafka.PersonConsumer;
import com.example.springcloudstreamexample.adapter.out.kafka.PersonSupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;

import java.util.function.Consumer;

@Slf4j
@SpringBootApplication
public class SpringCloudStreamExampleApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamExampleApplication.class, args);
    }

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private PersonSupplier personSupplier;

    @Bean
    public Consumer<String> log() {
        return message -> System.out.println("Log Received: " + message);
    }

    @EventListener
    public void handleContextStart(ApplicationStartedEvent ase) {
        System.out.println("Handling context started event.");

        personSupplier.get().then();

        int i = 0;
        while (true) {
            try {
                System.out.println("Log Send i - " + i);
                streamBridge.send("log-out-0", "hello " + i);

                System.out.println("Product Send i - " + i);
                streamBridge.send("product-in-0", "Product - " + i);
                i++;

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        /*context.registerBean("personConsumer", FunctionRegistration.class,
                () -> new FunctionRegistration<>(new PersonConsumer()).type(PersonConsumer.class));

        context.registerBean("personSupplier", FunctionRegistration.class,
                () -> new FunctionRegistration<>(new PersonSupplier()).type(PersonSupplier.class));*/
    }
}
