package com.vrana.database.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @Schema(description = "isbn of the book", example = "9780571206926", accessMode = Schema.AccessMode.READ_ONLY)
    private String isbn;

    @Schema(description = "title of the book", example = "Laughable Loves")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Schema(description = "author of the book")
    private AuthorDto author;
}