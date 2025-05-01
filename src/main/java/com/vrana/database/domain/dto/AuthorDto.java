package com.vrana.database.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(message = "Age must be a positive number", value = 0)
    private Integer age;
}
