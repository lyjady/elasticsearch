package com.example.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@SpringBootApplication
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

//    @Bean
//    public ElasticsearchTemplate elasticsearchTemplate() {
//        Client client = new NodeClient();
//        return new ElasticsearchTemplate();
//    }

}
