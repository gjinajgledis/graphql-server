package com.example.graphqlserver.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.stereotype.Service;

import com.example.graphqlserver.book.model.Book;
import com.example.graphqlserver.book.model.HelloInput;
import com.example.graphqlserver.book.model.HelloPayloadClient;
import com.example.graphqlserver.config.FlexRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExternalBookClient {

    @Autowired
    private FlexRequest flexRequest;

    @Autowired
    ObjectMapper objectMapper;

    public Book getBook() {
        return bookById();
    }

    public List<Book> books() {
        return getBooks();
    }

    String document =
            """
            query GetBook($bookId: ID!){
                bookById(id: $bookId) {
                    id
                    title
                    author {
                     id
                     name
                    }
                }
            }
            """;

    String books =
            """
            query BOOKS {
                books {
                    id
                    title
                }
            }
            """;

    //language=GraphQL
    String hello =
            """
            query {
                 hello(name: "%s")
            }
          """;


    public String getHello() {
        GraphQlClient.RequestSpec requestSpec = flexRequest.build(String.format(hello, "gledis"));
        return requestSpec
                .retrieveSync("hello")
                .toEntity(String.class);
    }

    public HelloPayloadClient getHelloInput() throws JsonProcessingException {
        String helloInput1 = objectMapper.writeValueAsString(new HelloInput("Weekly Groceries"));
        String json = "{ name: \"unquotedField\", id: 1}";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "\"helloInput1\"");
        map.put("id", 1);

        String helloInput = String.format("""
            mutation {
            helloInput(input: %s) {
               hello
               someProduct {
                    productId
               }
           }
         }
        """, json);

        return flexRequest.build(helloInput)
                .retrieveSync("helloInput")
                .toEntity(HelloPayloadClient.class);
    }

//    public List<ShoppingListDto> createLists() {
//        //language=GraphQL
//       final String CREATE_SHOPPING_LIST_MUTATION = """
//            mutation CreateShoppingList($shoppingListCreateInput: ShoppingListCreateInput!) {
//                shoppingListCreate(shoppingListCreateInput: $shoppingListCreateInput) {
//                    shoppingList {
//                        shoppingListId
//                        name
//                        productCount
//
//                    }
//                       failureReason
//                }
//            }
//            """;
//
//        return flexRequest.build(CREATE_SHOPPING_LIST_MUTATION)
//                .variable("shoppingListCreateInput", of("productIds",List.of("54d45f38-fffd-4a6e-b528-d164addd05de"),"name","new list"))
//                .retrieveSync("shoppingListCreate")
//                .toEntityList(ShoppingListDto.class);
//    }


    private Book bookById() {

        return flexRequest.build(document)
                .variable("bookId", "book-1")
                .operationName("GetBook")
                .retrieveSync("bookById")
                .toEntity(Book.class);
    }

    private List<Book> getBooks() {

        return flexRequest.build(books)
                .operationName("BOOKS")
                .retrieveSync("books")
                .toEntityList(Book.class);
    }
}
