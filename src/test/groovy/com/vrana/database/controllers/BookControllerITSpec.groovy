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
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class BookControllerITSpec extends Specification {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private BookService bookService

    def "connection to postgresql test container established"() {
        expect: "postgresql test container was created successfully and is running"
        postgres.isCreated()
        postgres.isRunning()
    }

    def "CreateBook returns HttpStatus 201 CREATED and the body contains the created book"() {
        given: "a new book DTO"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()

        and: "the book DTO as a JSON string"
        def bookAJson = objectMapper.writeValueAsString(testBookDtoA)

        when: "a POST request is made to create the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/books/" + testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
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
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA(null)
        bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        when: "a GET request is made to retrieve a pageable of books"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response is a page of books with matching book body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.content').isArray())
        result.andExpect(MockMvcResultMatchers.jsonPath('$.content[0].isbn').value(testBookDtoA.getIsbn()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.content[0].title').value(testBookDtoA.getTitle()))
    }

    def "GetBook returns HttpStatus 200 OK when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        when: "a GET request is made to retrieve the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "GetBook returns HttpStatus 404 NOT FOUND when book does not exist"() {
        when: "a GET request is made to retrieve a non-existing book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/NON-EXISTING-BOOK")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "GetBook returns correct book when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        when: "a GET request is made to retrieve the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the saved book body matches the retrieved book body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.isbn').value(createdBookA.getIsbn()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value(createdBookA.getTitle()))
    }

    def "FullUpdateBook returns HttpStatus 200 OK when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        and: "a different book body represented as a JSON string"
        def testBookDtoB = TestDataUtil.createTestBookDtoB()
        testBookDtoB.setIsbn(testBookDtoA.getIsbn())
        def bookBJson = objectMapper.writeValueAsString(testBookDtoB)

        when: "a PUT request is made to update saved bookB with bookA body"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBJson)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "FullUpdateBook returns HttpStatus 404 NOT FOUND when book does not exist"() {
        given: "a book body represented as a JSON string"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def bookAJson = objectMapper.writeValueAsString(testBookDtoA)

        when: "a PUT request is made to update a non-existing book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/books/NON-EXISTING-BOOK")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "FullUpdateBook returns correct book when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        and: "a different book body represented as a JSON string"
        def testBookDtoB = TestDataUtil.createTestBookDtoB()
        testBookDtoB.setIsbn(testBookDtoA.getIsbn())
        def bookBJson = objectMapper.writeValueAsString(testBookDtoB)

        when: "a PUT request is made to update the book's title"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBJson)
        )

        then: "the saved book body matches the retrieved book body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.isbn').value(createdBookA.getIsbn()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value(testBookDtoB.getTitle()))
    }


    def "PartialUpdateBook returns HttpStatus 200 OK when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        and: "the book's title is changed and the book is represented as a JSON string"
        testBookDtoA.setTitle("UPDATED")
        def bookAJson = objectMapper.writeValueAsString(testBookDtoA)

        when: "a PATCH request is made to update the saved books's title"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "PartialUpdateBook returns HttpStatus 404 NOT FOUND when book does not exist"() {
        given: "a book body represented as a JSON string"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def bookAJson = objectMapper.writeValueAsString(testBookDtoA)

        when: "a PATCH request is made to partially update a non-existing book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/NON-EXISTING-BOOK")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "PartialUpdateBook returns correct updated book when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        and: "the book's title is changed and the book is represented as a JSON string"
        testBookDtoA.setTitle("UPDATED")
        def bookAJson = objectMapper.writeValueAsString(testBookDtoA)

        when: "a PATCH request is made to update the saved books's title"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAJson)
        )

        then: "the saved book body matches the retrieved updated book body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.isbn').value(createdBookA.getIsbn()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.title').value(testBookDtoA.getTitle()))
    }


    def "DeleteBook returns HttpStatus 204 NO CONTENT when book exists"() {
        given: "a new book is saved to the repository"
        def testBookDtoA = TestDataUtil.createTestBookDtoA()
        def createdBookA = bookService.createBook(testBookDtoA.getIsbn(), testBookDtoA)

        when: "a DELETE request is made to delete the book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/${createdBookA.getIsbn()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 204 NO CONTENT"
        result.andExpect(MockMvcResultMatchers.status().isNoContent())
    }

    def "DeleteBook returns HttpStatus 204 NO CONTENT when book does not exists"() {
        when: "a DELETE request is made to delete a non-existing book"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/NON-EXISTING-BOOK")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 204 NO CONTENT"
        result.andExpect(MockMvcResultMatchers.status().isNoContent())
    }
}