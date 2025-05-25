package com.vrana.database.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @Schema(description = "isbn 13", example = "9780571206926", accessMode = Schema.AccessMode.READ_ONLY)
    private String isbn;

    @Schema(description = "openlibrary key", example = "/books/OL27069473M")
    private String key;

    @Schema(description = "title", example = "Laughable Loves")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Schema(description = "subtitle", example = "When Love Gets a Little Silly")
    private String subtitle;

    @Schema(description = "isbn 10", example = "0153788119")
    private List<String> isbn10;

    @Schema(description = "number of pages", example = "287")
    private Integer pageNumber;

    @Schema(description = "publish date", example = "1999")
    private String publishDate;

    @Schema(description = "publish country", example = "enk")
    private String publishCountry;

    @Schema(description = "publish places", example = "London")
    private List<String> publishPlaces;

    @Schema(description = "publishers", example = "Faber")
    private List<String> publishers;

    @Schema(description = "subjects", example = "Social life and customs")
    private List<String> subjects;

    @Schema(description = "contributions", example = "Rappaport, Suzanne")
    private List<String> contributions;

    @Schema(description = "author of the book")
    private AuthorDto author;
}