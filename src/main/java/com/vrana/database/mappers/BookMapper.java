package com.vrana.database.mappers;

import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.domain.entities.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto mapTo(BookEntity bookEntity);

    BookEntity mapFrom(BookDto bookDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "isbn", ignore = true)
    void updateFullBookFromDto(BookDto bookDto, @MappingTarget BookEntity bookEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "isbn", ignore = true)
    void updatePartialBookFromDto(BookDto bookDto, @MappingTarget BookEntity bookEntity);
}