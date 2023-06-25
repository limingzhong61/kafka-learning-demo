package com.lmz.client;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author: limingzhong
 * @create: 2023-06-24 10:32
 */
//消息的发送⽅
public class MyProducer {
    private final static String TOPIC_NAME = "my-replicated-topic";




    public static void main(String[] args) throws ExecutionException,
            InterruptedException {
        //1.设置参数
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "192.168.142.128:9092,192.168.142.128:9093,192.168.142.128:9094");

        /**
         * 发送消息需要到达何种持久化机制后，kafka才发送ack给生产者
         */
        props.put(ProducerConfig.ACKS_CONFIG,"1");

        //把发送的key从字符串序列化为字节数组
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        //把发送消息value从字符串序列化为字节数组
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        //2.创建生成消息的客服端,传入参数
        Producer<String, String> producer = new KafkaProducer<String,
                String>(props);

        int msgNum = 5;
        for(int i = 0; i < msgNum; i++){
            //3.创建消息
            // key: 作用是决定了往那个分区上发，value：具体要发送的消息
            ProducerRecord<String, String> producerRecord = new
                    ProducerRecord<String, String>(TOPIC_NAME
                    , String.valueOf(i),"hello,kafka" + i);
            //4.发送消息，得到消息发送的元数据 ；         等待消息发送成功的同步阻塞⽅法
            RecordMetadata metadata = producer.send(producerRecord).get();
            //=====阻塞=======
            System.out.println("同步⽅式发送消息结果：" + "topic-" +
                    metadata.topic() + "|partition-"
                    + metadata.partition() + "|offset-" +
                    metadata.offset());
        }




    }
}