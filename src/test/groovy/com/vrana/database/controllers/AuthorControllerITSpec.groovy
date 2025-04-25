package com.vrana.database.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.vrana.database.TestDataUtil
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

    def "connection to postgresql test container established"() {
        expect:
        postgres.isCreated()
        postgres.isRunning()
    }

    def "CreateAuthor returns HttpStatus 201 CREATED"() {
        given: "a new author DTO"
        def testAuthorA = TestDataUtil.createTestAuthorEntityA()
        testAuthorA.setId(null)

        and: "the author DTO as a JSON string"
        def authorJson = objectMapper.writeValueAsString(testAuthorA)

        when: "a POST request is made to create the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        )

        then: "the response status is CREATED"
        result.andExpect(MockMvcResultMatchers.status().isCreated())
    }

    def "CreateAuthor successfully returns saved author"() {
        given: "a new author DTO"
        def testAuthorA = TestDataUtil.createTestAuthorEntityA()
        testAuthorA.setId(null)

        and: "the author DTO as a JSON string"
        def authorJson = objectMapper.writeValueAsString(testAuthorA)

        when: "a POST request is made to create the author"
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        )

        then: "the body contains the created author"
        result.andExpect(MockMvcResultMatchers.jsonPath('$.id').isNumber())
        result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value(testAuthorA.getName()))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.age').value(testAuthorA.getAge()))
    }


}