package com.vrana.database.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAuthorResponse {
    private String key;
    private String name;
    @JsonProperty("birth_date")
    private String birthDate;
}