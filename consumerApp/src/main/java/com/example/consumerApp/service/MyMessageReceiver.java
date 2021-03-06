package com.example.consumerApp.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class MyMessageReceiver {

    private Logger LOGGER = LoggerFactory.getLogger(MyMessageReceiver.class);

    private Counter kafkaMsgArrivesCounter;

    private MeterRegistry registry;

    @Autowired
    public MyMessageReceiver(MeterRegistry registry){
        this.registry = registry;

        LOGGER.info("registry is not null! {}", registry);
    }

    @PostConstruct
    public void setCOunters(){
        kafkaMsgArrivesCounter =  Counter
                .builder("kafkaMsgArrived")
                .description("indicates instance count of the object")
                .tags("richard", "dev")
                .register(registry);
        LOGGER.info("counter is not null! {}", kafkaMsgArrivesCounter.count());
    }

    @KafkaListener(topics = "testTopic", groupId = "group_id")
    public void consume(String message) throws IOException {
        System.out.println(String.format("#### -> Consumed message -> %s", message));
        LOGGER.info("increment possible! {}", kafkaMsgArrivesCounter.count());
        kafkaMsgArrivesCounter.increment(1);
        LOGGER.info("increment possible! {}", kafkaMsgArrivesCounter.count());
    }

    public double getCount(){
        return this.kafkaMsgArrivesCounter.count();
    }

}
