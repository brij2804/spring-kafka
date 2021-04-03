package com.brijesh.microservices.twittertokafkaservice.runner;

import twitter4j.TwitterException;

public interface StreamRunner {
    void start() throws TwitterException;
}
