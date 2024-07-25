package com.example.graphqlserver.book;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.book.client.BookClient;
import com.example.graphqlserver.book.model.Book;
import com.example.graphqlserver.book.model.Paper;
import com.example.graphqlserver.book.transformer.BookTransformer;
import com.example.graphqlserver.book.transport.BookDto;

@Controller
public class BookQuery {

    private final BookClient bookClient;
    private final BookTransformer bookTransformer;

    public BookQuery(BookClient bookClient, BookTransformer bookTransformer) {
        this.bookClient = bookClient;
        this.bookTransformer = bookTransformer;
    }

    @QueryMapping
    public Book bookById(@Argument String id) {
        BookDto bookDto = bookClient.getById(id);
        return bookTransformer.transform(bookDto);
    }

    @QueryMapping
    public Paper papers() {
        return new Paper("test", "author-1");
    }

    @QueryMapping
    public List<Book> books() {
        List<BookDto> bookDto = bookClient.getBooks();
        return bookTransformer.transform(bookDto);
    }
}
