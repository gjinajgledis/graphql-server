package com.example.graphqlserver.author.transformer;

import org.springframework.stereotype.Component;

import com.example.graphqlserver.author.model.Author;
import com.example.graphqlserver.author.transport.AuthorDto;

@Component
public class AuthorTransformer {

    public Author transform(AuthorDto book) {
        return new Author(book.id(), book.name());
    }
}
