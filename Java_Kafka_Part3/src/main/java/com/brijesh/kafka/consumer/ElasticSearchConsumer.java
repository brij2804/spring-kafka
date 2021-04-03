package com.brijesh.kafka.consumer;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.message.ListOffsetRequestData;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ElasticSearchConsumer {

    Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class.getName());

    public static RestHighLevelClient createClient(){

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        return client;
    }

    public void addingDataToElasticSearch(){
        // create elastic search client
        RestHighLevelClient client = createClient();

        //create json message
        String jsonString= "{ \"foo\":\"bar\" }";

        IndexRequest indexRequest= new IndexRequest("twitter");
        indexRequest.source(jsonString, XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            String id = indexResponse.getId();
            logger.info(id);

            //close the client
            client.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addDataToElasticFromKafka(){

        // create elastic search client
        RestHighLevelClient client = createClient();

        // create kafka consumer
        KafkaConsumer<String,String> consumer = createKafkaConsumer();

        //poll for data
        while(true){
            ConsumerRecords<String,String> records =consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String,String> record:records) {
                // insert data into elastic search

                // create json string
                String jsonString= record.value(); // twitter data
                // create index request for es
                IndexRequest indexRequest= new IndexRequest("twitter");
                indexRequest.source(jsonString, XContentType.JSON);
                try {
                    IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                    String id = indexResponse.getId();
                    logger.info(id);
                    Thread.sleep(1000);

                }catch (IOException | InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void idempotentConsumer(){
        // create elastic search client
        RestHighLevelClient client = createClient();

        // create kafka consumer
        KafkaConsumer<String,String> consumer = createKafkaConsumer();

        //poll for data
        while(true){
            ConsumerRecords<String,String> records =consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String,String> record:records) {

                // 2 strategies for making consumer idempotent
                // kafka generic id
                String id = record.topic()+"_"+record.partition()+"_"+ record.offset();

                // insert data into elastic search
                // create json string
                String jsonString= record.value(); // twitter data
                // create index request for es
                IndexRequest indexRequest= new IndexRequest("twitter");
                indexRequest.id(id); // setting id to make it idempotent
                indexRequest.source(jsonString, XContentType.JSON);
                try {
                    IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                    String idnew = indexResponse.getId();
                    logger.info(idnew);
                    Thread.sleep(1000);

                }catch (IOException | InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public KafkaConsumer<String,String> createKafkaConsumer(){

        String bootstrapServer="127.0.0.1:9092";
        String groupId="kafka-elasticsearch";
        String topic="twitter_tweets";

        // create consumer config
        Properties properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        //create a consumer
        KafkaConsumer<String,String> consumer=new KafkaConsumer<String,String>(properties);
         consumer.subscribe(Arrays.asList(topic));
         return  consumer;
    }

    public static void main(String[] args) {

     ElasticSearchConsumer elasticSearchConsumer= new ElasticSearchConsumer();
     elasticSearchConsumer.idempotentConsumer();

    }

}
