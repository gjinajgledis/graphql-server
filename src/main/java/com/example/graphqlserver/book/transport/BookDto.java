package com.example.graphqlserver.book.transport;

public record BookDto(String id, String title, int pages, String authorId) {
}
