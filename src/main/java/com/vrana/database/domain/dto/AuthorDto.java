package com.vrana.database.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    @Schema(description = "id of the author", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "name of the author", example = "John Doe")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "age of the author", example = "35")
    @Min(message = "Age must be a positive number", value = 0)
    private Integer age;
}
