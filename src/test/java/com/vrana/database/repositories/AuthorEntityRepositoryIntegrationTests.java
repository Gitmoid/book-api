package com.vrana.database.repositories;

import com.vrana.database.TestDataUtil;
import com.vrana.database.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private AuthorRepository underTest;

    @Test
    public void testThatConnectionToPostgresqlTestContainerIsEstablished() {
        postgres.isCreated();
        postgres.isRunning();
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthorA = underTest.save(testAuthorEntityA);

        Optional<AuthorEntity> result = underTest.findById(savedAuthorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testAuthorEntityA);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorEntityB();
        AuthorEntity testAuthorEntityC = TestDataUtil.createTestAuthorEntityC();

        AuthorEntity savedAuthorA = underTest.save(testAuthorEntityA);
        AuthorEntity savedAuthorB = underTest.save(testAuthorEntityB);
        AuthorEntity savedAuthorC = underTest.save(testAuthorEntityC);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(savedAuthorA, savedAuthorB, savedAuthorC);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(testAuthorEntityA);
        testAuthorEntityA.setName("UPDATED");
        AuthorEntity savedAuthorA = underTest.save(testAuthorEntityA);

        Optional<AuthorEntity> result = underTest.findById(savedAuthorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedAuthorA);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthorA = underTest.save(testAuthorEntityA);
        underTest.deleteById(savedAuthorA.getId());

        Optional<AuthorEntity> result = underTest.findById(savedAuthorA.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorEntityB();
        AuthorEntity testAuthorEntityC = TestDataUtil.createTestAuthorEntityC();

        underTest.save(testAuthorEntityA);
        AuthorEntity savedAuthorB = underTest.save(testAuthorEntityB);
        AuthorEntity savedAuthorC = underTest.save(testAuthorEntityC);

        Iterable<AuthorEntity> result = underTest.findByAgeLessThan(50);
        assertThat(result).containsExactly(savedAuthorB, savedAuthorC);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan() {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorEntityB();
        AuthorEntity testAuthorEntityC = TestDataUtil.createTestAuthorEntityC();

        AuthorEntity savedAuthorA = underTest.save(testAuthorEntityA);
        underTest.save(testAuthorEntityB);
        underTest.save(testAuthorEntityC);

        Iterable<AuthorEntity> result = underTest.findByAgeGreaterThan(50);
        assertThat(result).containsExactly(savedAuthorA);
    }
}