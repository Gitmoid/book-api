package com.vrana.database;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log
public class BookApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
	}
}