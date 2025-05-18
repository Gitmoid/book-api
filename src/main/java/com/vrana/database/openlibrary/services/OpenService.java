package com.vrana.database.openlibrary.services;

import com.vrana.database.openlibrary.dto.OpenAuthorResponse;
import com.vrana.database.openlibrary.dto.OpenBookResponse;

public interface OpenService {

    OpenBookResponse getOpenBookByIsbn(String isbn);

    OpenAuthorResponse getOpenAuthorByKey();
}
