package com.example.graphqlserver.book.model;

import com.example.graphqlserver.author.model.Author;

public record Book(String id, String title, String authorId, Author author) {

}
