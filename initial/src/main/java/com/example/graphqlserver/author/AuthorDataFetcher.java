package com.example.graphqlserver.author;

import org.springframework.stereotype.Service;

import com.example.graphqlserver.author.transformer.AuthorTransformer;

@Service
public class AuthorDataFetcher {

    private final AuthorTransformer authorTransformer;
    public AuthorDataFetcher(AuthorTransformer authorTransformer) {
        this.authorTransformer = authorTransformer;
    }

}
