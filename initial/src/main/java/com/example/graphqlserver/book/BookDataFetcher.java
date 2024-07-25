package com.example.graphqlserver.book;

import java.util.concurrent.CompletableFuture;

import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.author.model.Author;
import com.example.graphqlserver.book.model.Book;

@Controller
public class BookDataFetcher {
    private static final Logger log = LoggerFactory.getLogger(BookDataFetcher.class);

    @SchemaMapping
    public CompletableFuture<Author> author(Book book, DataLoader<String, Author> dataLoader) {
        log.info("Resolving author for bookId {}", book.authorId());
        return dataLoader.load(book.authorId());
    }

}
