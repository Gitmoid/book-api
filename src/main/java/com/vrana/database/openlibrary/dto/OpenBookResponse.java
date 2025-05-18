package com.vrana.database.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OpenBookResponse {
    private String title;
    private List<AuthorRef> authors;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthorRef {
        private String key;
    }
}
