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
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class AuthorControllerITSpec extends Specification {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private AuthorService authorService

    def "connection to postgresql test container established"() {
        expect: "postgresql test container was created successfully and is running"
        postgres.isCreated()
        postgres.isRunning()
    }

    def "CreateAuthor returns HttpStatus 201 CREATED and the body contains the created author"() {
        given: "a new author DTO"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()

        and: "the author DTO as a JSON string"
        def authorAJson = objectMapper.writeValueAsString(testAuthorDtoA)

        when: "a POST request is made to create the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorAJson)
        )

        then:
        verifyAll {
            "the response status is 201 CREATED"
            result.andExpect(MockMvcResultMatchers.status().isCreated())

            "the body contains the created author"
            result.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNumber())
            result.andExpect(MockMvcResultMatchers.jsonPath('$.key').value(testAuthorDtoA.getKey()))
            result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value(testAuthorDtoA.getName()))
            result.andExpect(MockMvcResultMatchers.jsonPath('$.birthYear').value(testAuthorDtoA.getBirthYear()))
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
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        authorService.createAuthor(testAuthorDtoA)

        when: "a GET request is made to retrieve all authors"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response is a list of authors with matching author body"
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].id').isNumber())
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].key').value(testAuthorDtoA.getKey()))
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].name').value(testAuthorDtoA.getName()))
        result.andExpect(MockMvcResultMatchers.jsonPath('[0].birthYear').value(testAuthorDtoA.getBirthYear()))
    }

    def "GetAuthor returns HttpStatus 200 OK when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        when: "a GET request is made to retrieve the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "GetAuthor returns HttpStatus 404 NOT FOUND when author does not exist"() {
        when: "a GET request is made to retrieve a non-existing author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "GetAuthor returns correct author when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        when: "a GET request is made to retrieve the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the saved author body matches the retrieved author body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.id').value(createdAuthorA.getId()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.key').value(testAuthorDtoA.getKey()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value(testAuthorDtoA.getName()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.birthYear').value(testAuthorDtoA.getBirthYear()))
    }

    def "FullUpdateAuthor returns HttpStatus 200 OK when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        and: "a different author body represented as a JSON string"
        def testAuthorDtoB = TestDataUtil.createTestAuthorDtoB()
        def authorBJson = objectMapper.writeValueAsString(testAuthorDtoB)

        when: "a PUT request is made to update saved authorB with authorA body"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorBJson)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "FullUpdateAuthor returns HttpStatus 404 NOT FOUND when author does not exist"() {
        given: "an author body represented as a JSON string"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def authorAJson = objectMapper.writeValueAsString(testAuthorDtoA)

        when: "a PUT request is made to update a non-existing author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorAJson)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "FullUpdateAuthor returns correct author when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        and: "a different author body represented as a JSON string"
        def testAuthorDtoB = TestDataUtil.createTestAuthorDtoB()
        def authorBJson = objectMapper.writeValueAsString(testAuthorDtoB)

        when: "a PUT request is made to update saved authorB with authorA body"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorBJson)
        )

        then: "the updated author body matches the retrieved author body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.id').value(createdAuthorA.getId()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.key').value(testAuthorDtoB.getKey()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value(testAuthorDtoB.getName()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.birthYear').value(testAuthorDtoB.getBirthYear()))
    }

    def "PartialUpdateAuthor returns HttpStatus 200 OK when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        and: "the authors name is changed and the author is represented as a JSON string"
        testAuthorDtoA.setName("UPDATED")
        def authorAJson = objectMapper.writeValueAsString(testAuthorDtoA)

        when: "a PATCH request is made to update the saved author's name"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorAJson)
        )

        then: "the response status is 200 OK"
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "PartialUpdateAuthor returns HttpStatus 404 NOT FOUND when author does not exist"() {
        given: "an author body represented as a JSON string"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def authorAJson = objectMapper.writeValueAsString(testAuthorDtoA)

        when: "a PATCH request is made to update a non-existing author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorAJson)
        )

        then: "the response status is 404 NOT FOUND"
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def "PartialUpdateAuthor returns correct author when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        and: "the authors name is changed and the author is represented as a JSON string"
        testAuthorDtoA.setName("UPDATED")
        def authorAJson = objectMapper.writeValueAsString(testAuthorDtoA)

        when: "a PATCH request is made to update the saved author's name"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorAJson)
        )

        then: "the saved author body matches the retrieved author body"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.id').value(createdAuthorA.getId()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.key').value(testAuthorDtoA.getKey()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value(testAuthorDtoA.getName()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.birthYear').value(testAuthorDtoA.getBirthYear()))
    }

    def "DeleteAuthor returns HttpStatus 204 NO CONTENT when author exists"() {
        given: "a new author is saved to the repository"
        def testAuthorDtoA = TestDataUtil.createTestAuthorDtoA()
        def createdAuthorA = authorService.createAuthor(testAuthorDtoA)

        when: "a DELETE request is made to delete the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/${createdAuthorA.getId()}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 204 NO CONTENT"
        result.andExpect(MockMvcResultMatchers.status().isNoContent())
    }

    def "DeleteAuthor returns HttpStatus 204 NO CONTENT when author does not exists"() {
        when: "a DELETE request is made to delete a non-existing author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "the response status is 204 NO CONTENT"
        result.andExpect(MockMvcResultMatchers.status().isNoContent())
    }
}