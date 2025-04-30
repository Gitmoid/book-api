package com.vrana.database.repositories;

import com.vrana.database.TestDataUtil;
import com.vrana.database.domain.entities.AuthorEntity;
import com.vrana.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BookEntityRepositoryIntegrationTests {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private BookRepository underTest;

    @Test
    public void testThatConnectionToPostgresqlTestContainerIsEstablished() {
        postgres.isCreated();
        postgres.isRunning();
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorEntityA);
        BookEntity savedBookA = underTest.save(testBookEntityA);
        Optional<BookEntity> result = underTest.findById(savedBookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookA);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorEntityA);
        BookEntity testBookEntityB = TestDataUtil.createTestBookEntityB(testAuthorEntityA);
        BookEntity testBookEntityC = TestDataUtil.createTestBookEntityC(testAuthorEntityA);

        BookEntity savedBookA = underTest.save(testBookEntityA);
        BookEntity savedBookB = underTest.save(testBookEntityB);
        BookEntity savedBookC = underTest.save(testBookEntityC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(savedBookA, savedBookB, savedBookC);
    }


    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(testBookEntityA);

        testBookEntityA.setTitle("UPDATED");
        BookEntity savedBookA = underTest.save(testBookEntityA);

        Optional<BookEntity> result = underTest.findById(savedBookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookA);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);
        BookEntity savedBookA = underTest.save(testBookEntityA);

        underTest.deleteById(savedBookA.getIsbn());
        Optional<BookEntity> result = underTest.findById(savedBookA.getIsbn());
        assertThat(result).isEmpty();
    }
}