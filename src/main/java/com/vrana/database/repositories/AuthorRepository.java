package com.vrana.database.repositories;

import com.vrana.database.domain.entities.AuthorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {

    Iterable<AuthorEntity> findByAgeLessThan(int age);

    Iterable<AuthorEntity> findByAgeGreaterThan(int age);
}
