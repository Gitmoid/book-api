package com.vrana.database.mappers;

public interface BookMapper<E, D> {

    D mapTo(E e);

    E mapFrom(D d, String isbn);
}
