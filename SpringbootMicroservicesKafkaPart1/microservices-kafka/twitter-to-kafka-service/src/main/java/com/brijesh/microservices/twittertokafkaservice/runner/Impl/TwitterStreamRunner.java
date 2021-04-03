package com.brijesh.microservices.twittertokafkaservice.runner.Impl;


import com.brijesh.microservices.configserver.config.ConfigurationData;
import com.brijesh.microservices.twittertokafkaservice.listener.TwitterStatusListener;
import com.brijesh.microservices.twittertokafkaservice.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Component
public class TwitterStreamRunner implements StreamRunner {

    private Logger logger= LoggerFactory.getLogger(TwitterStreamRunner.class);

    @Autowired
    private TwitterStatusListener twitterStatusListener;

    @Autowired
    private ConfigurationData configurationData;

    private TwitterStream twitterStream;

    @Override
    public void start() throws TwitterException{
      twitterStream= new TwitterStreamFactory().getInstance();
      twitterStream.addListener(twitterStatusListener);
        addFilter();
    }
    @PreDestroy
    public void shutdown(){
      if(twitterStream!=null){
          logger.info("Closing twitter stream");
          twitterStream.shutdown();
      }
    }

    private void addFilter() {
        String[] keywords= configurationData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery= new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        logger.info("Started filtering twitter stream for keywords {}", Arrays.toString(keywords));
    }
}
