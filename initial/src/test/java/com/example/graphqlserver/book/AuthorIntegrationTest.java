package com.example.graphqlserver.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.UUID;
import javax.xml.crypto.Data;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.graphqlserver.book.model.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureGraphQlTester
@AutoConfigureMockMvc
public class AuthorIntegrationTest {


    @Autowired
    private GraphQlTester graphQlTester;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void bookById() {

        Book book =  this.graphQlTester
                .documentName("book")
                .operationName("byId")
                .variable("bookId","book-1")
                .execute()
                .path("data.bookById")
                .entity(Book.class)
                .get();
        assertEquals(book.id(), "book-1");
        assertEquals(book.author().name(), "Joshua Hyt");
    }

    @Test
    void applicationGraphQlBookById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                        .content("{ hello }")
                        .contentType("application/graphql")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.hello").value("Hello"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }
}
