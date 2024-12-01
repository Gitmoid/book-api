package com.vrana.database.repositories;

import com.vrana.database.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {

    Iterable<AuthorEntity> ageLessThan(int age);

    @Query("SELECT a from AuthorEntity a where a.age > ?1") // HQL annotation is needed
    Iterable<AuthorEntity> findAuthorsWithAgeGreaterThan(int age);
}
