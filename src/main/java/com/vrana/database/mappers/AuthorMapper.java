package com.vrana.database.mappers;

import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.domain.entities.AuthorEntity;
import com.vrana.database.openlibrary.dto.OpenAuthorResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto mapTo(AuthorEntity authorEntity);

    AuthorEntity mapFrom(AuthorDto authorDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "id", ignore = true)
    void updateFullAuthorFromDto(AuthorDto authorDto, @MappingTarget AuthorEntity authorEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updatePartialAuthorFromDto(AuthorDto authorDto, @MappingTarget AuthorEntity authorEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    AuthorDto mapDtoFromOpen(OpenAuthorResponse openAuthor);
}