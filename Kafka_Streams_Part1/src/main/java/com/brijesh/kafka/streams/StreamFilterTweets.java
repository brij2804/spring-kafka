package com.brijesh.kafka.streams;

import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class StreamFilterTweets {
    public static void main(String[] args) {
        // create properties
        Properties properties=new Properties();
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG,"demo-kafka-streams"); //similar to consumer group
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

        // create a topology
        StreamsBuilder streamsBuilder=new StreamsBuilder();

        //input topic to read data
        KStream<String,String> inputTopic=  streamsBuilder.stream("twitter_tweets");
        KStream<String,String> filteredStream = inputTopic.filter(
                (k,jsonTweet) -> extractUserFollowersInTweets(jsonTweet) > 10000
                        // filter for tweets which has a user of over 10000 followers
        );
        filteredStream.to("important_tweets");

        // build the topology
        KafkaStreams kafkaStreams= new KafkaStreams(streamsBuilder.build(),properties);

        // start our stream application
        kafkaStreams.start();

    }

    public static JsonParser jsonParser=new JsonParser();

    public static Integer extractUserFollowersInTweets(String tweetJson){
        try {
            return jsonParser.parse(tweetJson)
                    .getAsJsonObject()
                    .get("user")
                    .getAsJsonObject()
                    .get("followers_count")
                    .getAsInt();
        }catch(Exception e){
            return 0;
        }
    }
}
