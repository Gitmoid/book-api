package com.vrana.database.mappers.impl;

import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.domain.entities.AuthorEntity;
import com.vrana.database.mappers.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorMapperImpl implements AuthorMapper<AuthorEntity, AuthorDto> {

    private final ModelMapper modelMapper;

    @Override
    public AuthorDto mapTo(AuthorEntity authorEntity) {
        return modelMapper.map(authorEntity, AuthorDto.class);
    }

    @Override
    public AuthorEntity mapFrom(AuthorDto authorDto) {
        return modelMapper.map(authorDto, AuthorEntity.class);
    }
}
