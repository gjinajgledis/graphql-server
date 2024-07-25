package com.example.graphqlserver.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_GRAPHQL_RESPONSE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.graphqlserver.book.model.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
@AutoConfigureMockMvc
public class AuthorIntegrationTest {


    @Autowired
    private HttpGraphQlTester graphQlTester;
    @Autowired
    private MockMvc mockMvc;

    static final String BOOK_BY_ID_QUERY = "{" +
            "  bookById(id:\"book-1\") {" +
            "	id " +
            "  }" +
            "}";

    private final String BOOK_BY_ID_QUERY_2= """
            {
              "query": "query ($id: ID!) {\\n  bookById(id: $id) {\\n    id\\n  }\\n}",
              "variables": {
                "id": "book-1"
              }
            }
            """;


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
    public void applicationJsonBookById() {

        Book book =  this.graphQlTester
                .document(BOOK_BY_ID_QUERY)
                .execute()
                .path("data.bookById")
                .entity(Book.class)
                .get();
        assertEquals(book.id(), "book-1");
    }


    @Test
    void applicationJsonBookByIdMockmvc() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                        .content(BOOK_BY_ID_QUERY_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(APPLICATION_GRAPHQL_RESPONSE, MediaType.APPLICATION_GRAPHQL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.bookById.id").value("book-1"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void applicationGraphqlBookByIdMockmvc() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                        .content(BOOK_BY_ID_QUERY)
                        .contentType("application/graphql")
                        .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_GRAPHQL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.bookById.id").value("book-1"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }
}
