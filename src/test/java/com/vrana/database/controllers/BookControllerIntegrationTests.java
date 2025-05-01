package com.vrana.database.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrana.database.TestDataUtil;
import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
public class BookControllerIntegrationTests {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @Test
    public void testThatConnectionToPostgresqlTestContainerIsEstablished() {
        postgres.isCreated();
        postgres.isRunning();
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttpStatus201Created() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        String bookAJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsCreatedBook() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);
        String bookAJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDtoA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDtoA.getTitle())
        );
    }

    @Test
    public void testThatGetBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBooksReturnsPageOfBooks() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value("The Shadow in the Attic")
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExists() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404WhenNoBookExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/999-888-777")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(testBookDtoA.getIsbn());
        String bookBJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsHttpStatus404WhenNoBookExists() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        String bookAJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/999-888-777")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsUpdatedBook() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(testBookDtoA.getIsbn());
        String bookBJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDtoA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDtoB.getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(testBookDtoA.getIsbn());
        String bookBJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        testBookDtoA.setTitle("UPDATED");
        String bookAJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDtoA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204WhenNoBookExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/999-888-777")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204WhenBookExists() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}