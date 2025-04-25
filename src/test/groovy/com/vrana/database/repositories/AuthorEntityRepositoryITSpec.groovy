package com.vrana.database.repositories

import com.vrana.database.TestDataUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Specification

@DataJpaTest
@Testcontainers
class AuthorEntityRepositoryITSpec extends Specification {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")

    @Autowired
    private AuthorRepository underTest

    def "connection to postgresql test container established"() {
        expect: "postgresql test container was created successfully and is running"
        postgres.isCreated()
        postgres.isRunning()
    }

    def "author can be created and retrieved by ID"() {
        given: "a new author entity"
        def authorEntityA = TestDataUtil.createTestAuthorEntityA()

        when: "the author is saved to the database"
        underTest.save(authorEntityA)

        and: "the author is retrieved by its ID"
        def result = underTest.findById(authorEntityA.getId())

        then: "the retrieved authors should exist and match the original"
        verifyAll(result) {
            "the author is found in the database"
            isPresent()
            "the retrieved author matches all properties of the original author"
            get() == authorEntityA
        }
    }

    def "multiple authors can be created and recalled"() {
        given: "multiple new author entities"
        def authorEntityA = TestDataUtil.createTestAuthorEntityA()
        def authorEntityB = TestDataUtil.createTestAuthorEntityB()
        def authorEntityC = TestDataUtil.createTestAuthorEntityC()

        when: "all the authors are saved to the database"
        underTest.save(authorEntityA)
        underTest.save(authorEntityB)
        underTest.save(authorEntityC)

        and: "all the authors are retrieved from the database"
        def result = underTest.findAll()

        then: "the retrieved author should exist and match the original"
        verifyAll(result) {
            "the authors are found in the database"
            size() == 3
            "the retrieved authors contain all original authors"
            containsAll(authorEntityA, authorEntityB, authorEntityC)
        }
    }

    def "author can be updated and recalled"() {
        given: "a new author entity is saved to the database"
        def authorEntityA = TestDataUtil.createTestAuthorEntityA()
        underTest.save(authorEntityA)

        when: "the author's name is changed and the author is saved"
        authorEntityA.setName("UPDATED")
        underTest.save(authorEntityA)

        and: "the author is retrieved by its ID"
        def result = underTest.findById(authorEntityA.getId())

        then: "the retrieved author should exist and match the original"
        verifyAll(result) {
            "the author is found in the database"
            isPresent()
            "the retrieved author matches all properties of the updated author"
            get() == authorEntityA
        }
    }

    def "author can be deleted"() {
        given: "a new author entity is saved to the database"
        def authorEntityA = TestDataUtil.createTestAuthorEntityA()
        underTest.save(authorEntityA)

        when: "the author is deleted from the database"
        underTest.deleteById(authorEntityA.getId())

        and: "the author is retrieved by its ID"
        def result = underTest.findById(authorEntityA.getId())

        then: "the retrieved author should be empty"
        result.isEmpty()
    }
}