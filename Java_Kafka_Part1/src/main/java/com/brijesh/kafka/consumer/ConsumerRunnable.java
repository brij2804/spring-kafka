package com.brijesh.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerRunnable implements Runnable{

    private CountDownLatch latch;
    private KafkaConsumer<String,String> consumer;
    Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());

    public ConsumerRunnable(CountDownLatch latch, String topic, Properties properties){
       this.latch=latch;
        //create a consumer
        consumer=new KafkaConsumer<String,String>(properties);
        //subscribe consumer to a topic(s)
        consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run(){
        //poll for a new data
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Key: " + record.key() + ", Value: " + record.value());
                    logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
                }
            }
        }catch (WakeupException exp){
            logger.info("Recieved shutdown signal!!");
        }finally {
            consumer.close();
            // tell our main code we are done with the consumer
            latch.countDown();
        }
    }

    public void shutdown(){
        // the wakeup method is a special method to interrupt consumer.poll()
        //it will throw the exception WakeUpException
        consumer.wakeup();
    }

}
