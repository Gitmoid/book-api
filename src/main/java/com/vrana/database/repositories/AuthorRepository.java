package com.vrana.database.repositories;

import com.vrana.database.domain.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Iterable<Author> ageLessThan(int age);

    @Query("SELECT a from Author a where a.age > ?1") // HQL annotation is needed
    Iterable<Author> findAuthorsWithAgeGreaterThan(int age);
}
