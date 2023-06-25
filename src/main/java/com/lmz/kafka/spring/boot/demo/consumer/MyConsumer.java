package com.lmz.kafka.spring.boot.demo.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class MyConsumer {

    //@KafkaListener(topics = "my-replicated-topic",groupId = "MyGroup1")
    //public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
    //  String value = record.value();
    //  System.out.println(value);
    //  System.out.println(record);
    //  //手动提交offset
    //  ack.acknowledge();
    //}


    @KafkaListener(groupId = "testGroup", topicPartitions = {
            @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
            @TopicPartition(topic = "topic2", partitions = "0",
                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
    }, concurrency = "3")//concurrency就是同组下的消费者个数，就是并发消费数，建议小于等于分区总数
    public void listenGroupPro(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        //手动提交offset
        ack.acknowledge();
    }


  /*@KafkaListener(topics = "my-replicated-topic",groupId = "MyGroup2")
  public void listensGroup(ConsumerRecords<String, String> records, Acknowledgment ack) {
    String value = record.value();
    System.out.println(value);
    System.out.println(record);
    //手动提交offset
    ack.acknowledge();
  }*/

}
