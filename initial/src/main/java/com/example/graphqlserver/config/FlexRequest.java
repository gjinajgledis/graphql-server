package com.example.graphqlserver.config;

import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class FlexRequest {

    private final HttpSyncGraphQlClient graphQlClient;

    public FlexRequest(HttpSyncGraphQlClient graphQlClient) {
        this.graphQlClient = graphQlClient;
    }

    public GraphQlClient.RequestSpec build(String document) {

        return graphQlClient
                .mutate()
                .headers(httpHeaders -> httpHeaders.addAll(commonHeaders()))
                .build().document(document);
    }

    private HttpHeaders commonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("bannerId", "439caf8d-c672-4596-b3c3-9d3bde5c980b");
        headers.add("regionId", "979c0e65-8638-41b5-b5a8-a10d6a314e9d");
        headers.add("Accept-Currency","GBP");
        headers.add("Accept-Language","en-UK");
        headers.add("Visitor-Id","25db63ce-5b66-4497-9a5e-57574085563d");

        return headers;
    }
}
