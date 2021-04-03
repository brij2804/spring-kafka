package com.brijesh.microservices.twittertokafkaservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterStatusListener extends StatusAdapter {

    private Logger logger= LoggerFactory.getLogger(TwitterStatusListener.class);

    @Override
    public void onStatus(Status status){
        logger.info("Twitter status with text {}",status.getText());
    }
}
