package com.example.graphqlserver.book.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.graphqlserver.book.model.Book;
import com.example.graphqlserver.book.transport.BookDto;

@Component
public class BookTransformer {

    public Book transform(BookDto book) {
        return new Book(book.id(), book.title(), book.authorId(), null);
    }

    public List<Book> transform(List<BookDto> book) {
        return book.stream().map(this::transform).collect(Collectors.toList());
    }
}
