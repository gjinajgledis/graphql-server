package com.example.graphqlserver.author.client;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.graphqlserver.author.transport.AuthorDto;

@Component
public class AuthorClient {

    private static final List<AuthorDto> authors = Arrays.asList(
            new AuthorDto( "author-1", "Joshua Hyt"),
            new AuthorDto( "author-2", "Jonathan Tres"),
            new AuthorDto("author-3", "Some Name")
    );

    public AuthorDto getById(String id) {
        return authors.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<AuthorDto> getById(Set<String> idSet) {
        return authors.stream()
                .filter(book -> idSet.contains(book.id()))
                .collect(Collectors.toList());
    }

}