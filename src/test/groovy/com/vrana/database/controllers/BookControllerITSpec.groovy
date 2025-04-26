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
}