package com.brijesh.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread {

    Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());
    String bootstrapServer="127.0.0.1:9092";
    String groupId="my-sixth-application";
    String topic="first_topic";

    // create consumer config
    Properties properties;

    private ConsumerDemoWithThread(){
        // create consumer config
        properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
    }


    private void run(){

        //latch for dealing with multiple threads.
        CountDownLatch latch = new CountDownLatch(1);

        // create the consumer runnable
        logger.info("Creating the consumer thread");
        Runnable myConsumerRunnable = new ConsumerRunnable(latch,topic,properties);

        //start the thread
        Thread mythread =new Thread(myConsumerRunnable);
        mythread.start();

        //add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("Caught shutdown hook");
            ((ConsumerRunnable)myConsumerRunnable).shutdown();
            try {
                latch.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            logger.info("Application has exited");
        }
        ));

        try {
            latch.wait();
        }catch (InterruptedException e){
           logger.error("Application got interrupted",e);
        }finally{
            logger.info("Application is closing");
        }
    }

    public static void main(String[] args) {
        new ConsumerDemoWithThread().run();
    }


}
