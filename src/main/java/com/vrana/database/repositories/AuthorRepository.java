package com.vrana.database.repositories;

import com.vrana.database.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    Iterable<AuthorEntity> findByAgeLessThan(int age);

    Iterable<AuthorEntity> findByAgeGreaterThan(int age);
}
