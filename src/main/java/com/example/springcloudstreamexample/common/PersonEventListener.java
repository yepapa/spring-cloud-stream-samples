package com.example.springcloudstreamexample.common;

import com.example.springcloudstreamexample.adapter.out.kafka.PersonSupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonEventListener {

    private final PersonSupplier personSupplier;

    public PersonEventListener(PersonSupplier personSupplier) {
        this.personSupplier = personSupplier;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void handleContextStart(ApplicationReadyEvent are) {
        personSupplier.get().then();
    }
}
