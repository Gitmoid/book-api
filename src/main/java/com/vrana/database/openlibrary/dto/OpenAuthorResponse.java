package com.vrana.database.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenAuthorResponse {
    private String key;
    private String name;
    @JsonProperty("birth_date")
    private int birthYear;
}