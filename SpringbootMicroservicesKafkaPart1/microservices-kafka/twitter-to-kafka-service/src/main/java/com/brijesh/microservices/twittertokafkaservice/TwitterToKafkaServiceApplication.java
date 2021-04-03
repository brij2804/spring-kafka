package com.brijesh.microservices.twittertokafkaservice;


import com.brijesh.microservices.configserver.config.ConfigurationData;
import com.brijesh.microservices.twittertokafkaservice.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static Logger LOG= LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    @Autowired
    private ConfigurationData configurationData;

    @Autowired
    private StreamRunner streamRunner;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Appli is running");
        LOG.info(Arrays.toString(configurationData.getTwitterKeywords().toArray(new String[] {})));
        streamRunner.start();
    }
}
