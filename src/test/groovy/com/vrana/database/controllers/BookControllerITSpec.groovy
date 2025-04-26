package com.vrana.database.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.vrana.database.TestDataUtil
import com.vrana.database.services.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Specification


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class BookControllerITSpec extends Specification {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    BookService bookService

    def "connection to postgresql test container established"() {
        expect: "postgresql test container was created successfully and is running"
        postgres.isCreated()
        postgres.isRunning()
    }

    def "CreateUpdateBook returns HttpStatus 201 CREATED and the body contains the created book"() {
        given: "a new book DTO"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()

        and: "the book DTO as a JSON string"
        def bookJson = objectMapper.writeValueAsString(testBookDtoA)

        when: "a PUT request is made to create the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        )

        then:
        verifyAll {
            "the response status is 201 CREATED"
            result.andExpect(MockMvcResultMatchers.status().isCreated())

            "the body contains the created book"
            result.andExpect(MockMvcResultMatchers.jsonPath('$.isbn').value(testBookDtoA.getIsbn()))
            result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value(testBookDtoA.getTitle()))
        }
    }

    def "ListBooks returns HttpStatus 200 OK"() {
        when: "a GET request is made to retrieve all books"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "ListBooks returns a page of books with matching book body"() {
        given: "a new book is saved in the repository"
        def testBookEntityA = TestDataUtil.createTestBookEntityA()
        bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA)

        when: "a GET request is made to retrieve a pageable of books"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response is a page of books with matching book body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.content').isArray())
        result.andExpect(MockMvcResultMatchers.jsonPath('$.content[0].isbn').value(testBookEntityA.getIsbn()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.content[0].title').value(testBookEntityA.getTitle()))
    }

    def "GetBook returns HttpStatus 200 OK when book exists"() {
        given: "a new book is saved in the repository"
        def testBookEntityA = TestDataUtil.createTestBookEntityA()
        def savedBook = bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA)

        when: "a GET request is made to retrieve the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/${savedBook.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "GetBook returns HttpStatus 404 NOT FOUND when book does not exist"() {
        when: "a GET request is made to retrieve a non-existing book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/999-888-777")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "GetBook returns correct book when book exists"() {
        given: "a new book is saved in the repository"
        def testBookEntityA = TestDataUtil.createTestBookEntityA()
        def savedBook = bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA)

        when: "a GET request is made to retrieve the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/${savedBook.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the saved book body matches the retrieved book body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.isbn').value(savedBook.getIsbn()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value(savedBook.getTitle()))
    }
}