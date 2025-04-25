package com.vrana.database.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.vrana.database.TestDataUtil
import com.vrana.database.services.AuthorService
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
class AuthorControllerITSpec extends Specification {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    AuthorService authorService

    def "connection to postgresql test container established"() {
        expect: "postgresql test container was created successfully and is running"
        postgres.isCreated()
        postgres.isRunning()
    }

    def "CreateAuthor returns HttpStatus 201 CREATED"() {
        given: "a new author DTO"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()

        and: "the author DTO as a JSON string"
        def authorJson = objectMapper.writeValueAsString(testAuthorDtoA)

        when: "a POST request is made to create the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        )

        then:
        verifyAll {
            "the response status is 201 CREATED"
            result.andExpect(MockMvcResultMatchers.status().isCreated())

            "the body contains the created author"
            result.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNumber())
            result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value(testAuthorDtoA.getName()))
            result.andExpect(MockMvcResultMatchers.jsonPath('$.age').value(testAuthorDtoA.getAge()))
        }
    }

    def "ListAuthors returns HttpStatus 200 OK"() {
        when: "a GET request is made to retrieve all authors"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "ListAuthors returns a list of authors"() {
        given: "a new AuthorEntity saved in the repository"
        def testAuthorEntityA = TestDataUtil.createTestAuthorEntityA()
        authorService.saveAuthor(testAuthorEntityA)

        when: "a GET request is made to retrieve all authors"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response is a list of authors with matching author body"
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].id').isNumber())
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].name').value(testAuthorEntityA.getName()))
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].age').value(testAuthorEntityA.getAge()))
    }
}