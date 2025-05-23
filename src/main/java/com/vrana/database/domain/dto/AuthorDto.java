package com.vrana.database.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    @Schema(description = "id of the author", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "openlibrary key of the author", example = "PL4326321A")
    private String key;

    @Schema(description = "name of the author", example = "Milan Kundera")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "birth date of the author", example = "01 April 1929")
    private String birthDate;
}
