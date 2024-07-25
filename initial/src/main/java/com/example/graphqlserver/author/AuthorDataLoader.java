package com.example.graphqlserver.author;

import java.util.HashSet;
import java.util.List;

import org.dataloader.BatchLoaderEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;

import com.example.graphqlserver.author.client.AuthorClient;
import com.example.graphqlserver.author.model.Author;
import com.example.graphqlserver.author.transformer.AuthorTransformer;

import reactor.core.publisher.Flux;

@Configuration
public class AuthorDataLoader {

    private final AuthorTransformer authorTransformer;
    private final AuthorClient authorClient;

    public AuthorDataLoader(BatchLoaderRegistry loaderRegistry, AuthorTransformer authorTransformer, AuthorClient authorClient) {
        this.authorTransformer = authorTransformer;
        this.authorClient = authorClient;
        loaderRegistry.forTypePair(String.class, Author.class).registerBatchLoader(this::loadAuthor);
    }

    private Flux<Author> loadAuthor(List<String> ids, BatchLoaderEnvironment env){
        return Flux.fromIterable(authorClient.getById(new HashSet<>(ids))).map(authorTransformer::transform);
    }
}
