package com.vrana.database;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class BookApiApplicationTests {

	@Container
	@ServiceConnection
	private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@Test
	void contextLoads() {
	}

}