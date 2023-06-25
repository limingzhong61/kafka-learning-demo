package com.lmz.client;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @author: limingzhong
 * @create: 2023-06-24 14:25
 */
public class MyConsumer {
    private final static String TOPIC_NAME = "my-replicated-topic";
    private final static String CONSUMER_GROUP_NAME = "testGroup2";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.142.128:9092,192.168.142.128:9093,192.168.142.128:9094");
        // 消费分组名
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        //新消费组的消费偏移量offset规则
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //创建一个消费者的客户端
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);



        // 消费者订阅主题列表
        consumer.subscribe(Arrays.asList(TOPIC_NAME));


        //消息回溯消费
        //consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0 )));
        //consumer.seekToBeginning(Arrays.asList(new TopicPartition(TOPIC_NAME,0 )),10);

        //指定offset消费
        //consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0 )));
        //consumer.seek(new TopicPartition(TOPIC_NAME, 0 ), 10 );


        List<PartitionInfo> topicPartitions =consumer.partitionsFor(TOPIC_NAME);





        //从 1 小时前开始消费
        /*long fetchDataTime = new Date().getTime() - 1000 * 60 * 60 ;
        Map<TopicPartition, Long> map = new HashMap<>();
        for (PartitionInfo par : topicPartitions) {
            map.put(new TopicPartition(TOPIC_NAME, par.partition()),fetchDataTime);
        }
        Map<TopicPartition, OffsetAndTimestamp> parMap =consumer.offsetsForTimes(map);
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry :parMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndTimestamp value = entry.getValue();
            if (key == null || value == null) continue;
            Long offset = value.offset();
            System.out.println("partition-" + key.partition() +"|offset-" + offset);
            System.out.println();
            //根据消费里的timestamp确定offset
            if (value != null) {
                consumer.assign(Arrays.asList(key));
                consumer.seek(key, offset);
            }
        }*/


        /**
         * 设置提交offset的方式
         */
        // 是否自动提交offset，默认就是true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 自动提交offset的间隔时间
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");




        while (true) {
            /*
             * poll() API 是拉取消息的⻓轮询
             */
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("收到消息：partition = %d,offset = %d, key =%s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
            }

            // 所有的消息已经消费完
            if (records.count() > 0) {  // 有消息
                // 手动同步提交offset，当前线程会阻塞直到offset提交成功
                // 一般使用同步提交，因为提交之后一般也没有什么逻辑代码了
                consumer.commitSync();
                /*
                // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception != null) {
                            System.err.println("Commit failed for " + offsets);
                            System.err.println("Commit failed exception: " + exception.getStackTrace());
                        }
                    }
                });*/
            }
        }
    }
}
