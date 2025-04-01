package com.example.graphqlserver.book;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.book.client.BookClient;
import com.example.graphqlserver.book.model.Book;
import com.example.graphqlserver.book.model.HelloTest;
import com.example.graphqlserver.book.model.HelloPayload;
import com.example.graphqlserver.book.model.Paper;
import com.example.graphqlserver.book.model.SomeProduct;
import com.example.graphqlserver.book.transformer.BookTransformer;
import com.example.graphqlserver.book.transport.BookDto;

import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;

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
    public String hello(@Argument String name, DataFetchingEnvironment environment) {
        ResultPath path = environment.getExecutionStepInfo().getPath();
        System.out.println(path);
        return "Hello " + name;
    }

    @MutationMapping
    public HelloPayload helloInput(@Argument HelloTest input) {
        return new HelloPayload(input.name(),new SomeProduct(List.of(1,2,3)));
    }

    @QueryMapping
    public List<Book> books(DataFetchingEnvironment environment) {
        List<String> path = environment.getExecutionStepInfo()
                .getField()
                .getSingleField()
                .getSelectionSet()
                .getSelections()
                .stream()
                .map(s -> ((Field)s).getName())
                .toList();
        System.out.println("Fields list: " + path);
        List<BookDto> bookDto = bookClient.getBooks();
        return bookTransformer.transform(bookDto);
    }
}
