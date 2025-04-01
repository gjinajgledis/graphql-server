package com.example.graphqlserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

@Configuration
public class GraphqlClientFactory {

    @Bean
    public HttpSyncGraphQlClient graphqlClient(RestClient flexRestClient) {
        return HttpSyncGraphQlClient.builder(flexRestClient)
                .header("Authorization", "Bearer " + "<panda-token>")
                .build();
    }

    @Bean
    public RestClient flexRestClient() {
        return RestClient.create("http://localhost:8080/graphql");
    }

}
