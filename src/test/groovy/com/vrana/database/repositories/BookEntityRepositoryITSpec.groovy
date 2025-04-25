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
class BookEntityRepositoryITSpec extends Specification {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")

    @Autowired
    private BookRepository underTest

    def "connection to postgresql test container established"() {
        expect: "postgresql test container was created successfully and is running"
        postgres.isCreated()
        postgres.isRunning()
    }

    def "book can be created and retrieved by ISBN"() {
        given: "a new book entity"
        def bookEntityA = TestDataUtil.createTestBookEntityA()

        when: "the book is saved to the database"
        underTest.save(bookEntityA)

        and: "the book is retrieved by its ISBN"
        def result = underTest.findById(bookEntityA.getIsbn())

        then: "the retrieved book should exist and match the original"
        verifyAll(result) {
            "the book is found in the database"
            isPresent()
            "the retrieved book matches all properties of the original book"
            get() == bookEntityA
        }
    }

    def "multiple books can be created and recalled"() {
        given: "multiple new book entities"
        def bookEntityA = TestDataUtil.createTestBookEntityA()
        def bookEntityB = TestDataUtil.createTestBookEntityB()
        def bookEntityC = TestDataUtil.createTestBookEntityC()

        when: "all the books are saved to the database"
        underTest.save(bookEntityA)
        underTest.save(bookEntityB)
        underTest.save(bookEntityC)

        and: "all the books are retrieved from the database"
        def result = underTest.findAll()

        then: "the retrieved books should exist and match the original"
        verifyAll(result) {
            "the books are found in the database"
            size() == 3
            "the retrieved books contain all original books"
            containsAll(bookEntityA, bookEntityB, bookEntityC)
        }
    }

    def "book can be updated and recalled"() {
        given: "a new book entity is saved to the database"
        def bookEntityA = TestDataUtil.createTestBookEntityA()
        underTest.save(bookEntityA)

        when: "the book's title is changed and the book is saved"
        bookEntityA.setTitle("UPDATED")
        underTest.save(bookEntityA)

        and: "the book is retrieved by its ISBN"
        def result = underTest.findById(bookEntityA.getIsbn())

        then: "the retrieved book should exist and match the original"
        verifyAll(result) {
            "the book is found in the database"
            isPresent()
            "the retrieved book matches all properties of the updated book"
            get() == bookEntityA
        }
    }

    def "book can be deleted"() {
        given: "a new book entity is saved to the database"
        def bookEntityA = TestDataUtil.createTestBookEntityA()
        underTest.save(bookEntityA)

        when: "the book is deleted from the database"
        underTest.deleteById(bookEntityA.getIsbn())

        and: "the book is retrieved by its ISBN"
        def result = underTest.findById(bookEntityA.getIsbn())

        then: "the retrieved book should be empty"
        result.isEmpty()
    }
}