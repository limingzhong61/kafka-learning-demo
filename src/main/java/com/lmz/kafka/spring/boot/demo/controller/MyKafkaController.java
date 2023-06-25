package com.lmz.kafka.spring.boot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
public class MyKafkaController {

  private final static String TOPIC_NAME = "my-replicated-topic";

  @Autowired
  private KafkaTemplate<String,String> kafkaTemplate;

  @RequestMapping("/send")
  public String sendMessage(){

    kafkaTemplate.send(TOPIC_NAME,0,"key","this is a message!");

    return "send success!";

  }

  @RequestMapping("/send/{topic}")
  public String sendMessage(@PathVariable("topic") String topicName){

    kafkaTemplate.send(topicName,0,"key","this is a message!");

    return "send success!";

  }

}
