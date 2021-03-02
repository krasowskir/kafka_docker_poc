package com.example.demo.controller;

import com.example.demo.service.ContactProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ChatController {

    @Autowired
    private ContactProducer contactProducer;

    @RequestMapping(value = "/chat")
    public ResponseEntity<String> sendChatMessage() throws IOException, InterruptedException {
        contactProducer.chat();
        Thread.sleep(1000);
        return new ResponseEntity<String>("chat successfully", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/chatLoop")
    public ResponseEntity<String> sendChatMessagesInLoop(@RequestParam("loop") boolean loop,
                                                         @RequestParam("duration") int duration,
                                                         @RequestParam("sleep") int sleepMillis) throws IOException, InterruptedException {
        if (!loop) {
                contactProducer.chat();
            return new ResponseEntity<String>("chat successfully", HttpStatus.ACCEPTED);
        } else {
            int start = 0;
            while (start <= duration) {
                try {
                    contactProducer.chat();
                } catch (Exception e) {
                System.out.println("JmsException " + e.getMessage());
                }
                Thread.sleep(sleepMillis);
                start++;
            }
            return new ResponseEntity<String>("chat successfully", HttpStatus.ACCEPTED);
        }
    }

}
