package com.vrana.database.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrana.database.TestDataUtil;
import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttpStatus201Created() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        String testBookDtoAJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookDtoAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        String testBookDtoAJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookDtoAJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDtoA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDtoA.getTitle())
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsBook() throws Exception {
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
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookDtoA.getIsbn())
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
        String bookJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsUpdatedBook() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        BookDto testBookDtoB = TestDataUtil.createTestBookDtoB(null);
        testBookDtoB.setIsbn(testBookDtoA.getIsbn());
        String bookJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
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
        String bookJson = objectMapper.writeValueAsString(testBookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception {
        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA);

        testBookDtoA.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
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