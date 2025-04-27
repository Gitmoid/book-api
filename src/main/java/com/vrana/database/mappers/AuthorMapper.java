package com.vrana.database.mappers;

public interface AuthorMapper<E, D> {

    D mapTo(E e);

    E mapFrom(D d);
}
