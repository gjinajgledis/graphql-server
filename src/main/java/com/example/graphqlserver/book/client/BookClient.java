package com.example.graphqlserver.book.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.graphqlserver.book.transport.BookDto;

@Component
public class BookClient {

    private static final List<BookDto> books = Arrays.asList(
            new BookDto("book-1", "Effective Java", 416, "author-1"),
            new BookDto("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-1"),
            new BookDto("book-3", "Down Under", 436, "author-1")
    );

    public BookDto getById(String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<BookDto> getBooks(){
        return new ArrayList<>(books);
    }
}