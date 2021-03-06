package com.example.demo.service;

import com.example.demo.model.Address;
import com.example.demo.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContactProducer {

    @Autowired
    private ObjectMapper mapper;

    private static Integer MESSAGE_COUNTER = 0;

    @Autowired
    private KafkaTemplate<String, String> template;

    private final static String TOPIC = "testTopic";
    private Address address;
    private Player player;


    public ContactProducer() throws Exception{
        this.address = new Address("Goethestraße 14", "Dresden", 11390);
        this.player = new Player("gerrit", "manning", 123456789, address);

    }

    public void chat() throws IOException {
        template.send(TOPIC, mapper.writeValueAsString(player) + "---" + MESSAGE_COUNTER++);
    }
}
