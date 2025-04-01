package com.example.graphqlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.graphqlserver.client.ExternalBookClient;

@SpringBootApplication
public class GraphqlClientApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GraphqlClientApplication.class, args);
        ExternalBookClient externalBookClient = context.getBean(ExternalBookClient.class);
        System.out.println(externalBookClient.createLists());

    }

}
