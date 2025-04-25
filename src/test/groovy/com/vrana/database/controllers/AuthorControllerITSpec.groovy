package com.vrana.database.controllers

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
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
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")

    def "connection to postgresql test container established"() {
        expect:
        postgres.isCreated()
        postgres.isRunning()
    }
}