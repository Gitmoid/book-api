package com.vrana.database.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenBookResponse {
    private String key;
    private String title;
    private String subtitle;
    @JsonProperty("isbn_10")
    private List<String> isbn10;
    @JsonProperty("number_of_pages")
    private Integer pageNumber;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("publish_country")
    private String publishCountry;
    @JsonProperty("publish_places")
    private List<String> publishPlaces;
    private List<String> publishers;
    private List<String> subjects;
    private List<String> contributions;
    private List<AuthorRef> authors;
    private Type type;

    @Data
    @NoArgsConstructor
    public static class AuthorRef {
        private String key;
    }

    @Data
    @NoArgsConstructor
    public static class Type {
        private String key;
    }
}
