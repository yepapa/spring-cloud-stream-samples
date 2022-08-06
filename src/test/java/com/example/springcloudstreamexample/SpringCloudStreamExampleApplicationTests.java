package com.example.springcloudstreamexample;

import com.example.springcloudstreamexample.adapter.out.kafka.dto.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SpringCloudStreamExampleApplicationTests {

    @Test
    public void testMultipleFunctions() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        SpringCloudStreamExampleApplication.class))
                .run("--spring.cloud.function.definition=log;personSupplier;personConsumer")) {

            InputDestination inputDestination = context.getBean(InputDestination.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
            Message<Person> inputPersonMessage = MessageBuilder.withPayload(new Person("Jane")).build();
            inputDestination.send(inputMessage, "log-in-0");
            inputDestination.send(inputPersonMessage, "personSupplier-in-0");

            Message<byte[]> outputMessage = outputDestination.receive(0, "log-out-0");
            assertThat(outputMessage.getPayload()).isEqualTo("Hello".getBytes());

            outputMessage = outputDestination.receive(0, "personConsumer-out-1");
            assertThat(outputMessage.getPayload()).isEqualTo(inputPersonMessage);
        }
    }

}
