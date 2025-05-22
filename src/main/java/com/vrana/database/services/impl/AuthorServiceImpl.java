package com.vrana.database.services.impl;

import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.domain.entities.AuthorEntity;
import com.vrana.database.mappers.AuthorMapper;
import com.vrana.database.openlibrary.dto.OpenAuthorResponse;
import com.vrana.database.openlibrary.services.OpenService;
import com.vrana.database.repositories.AuthorRepository;
import com.vrana.database.services.AuthorService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final OpenService openService;

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        if (authorDto.getKey() != null) {
            authorRepository.findByKey(authorDto.getKey())
                    .ifPresent(author -> {
                        throw new EntityExistsException("Author with key " + authorDto.getKey() + " already exists");
                    });
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        return authorMapper.mapTo(savedAuthorEntity);
    }

    @Override
    public AuthorDto getOrCreateAuthor(AuthorDto authorDto) {
        return authorRepository.findByKey(authorDto.getKey())
                .map(authorMapper::mapTo)
                .orElseGet(() -> createAuthor(authorDto));
    }

    @Override
    public AuthorDto createOpenAuthor(String authorKey) {
        if (authorRepository.findByKey(authorKey).isPresent()) {
            throw new EntityExistsException("Author with key " + authorKey + " already exists");
        }

        OpenAuthorResponse openAuthor = openService.getOpenAuthorByKey(authorKey);
        AuthorDto authorDto = authorMapper.mapDtoFromOpen(openAuthor);
        authorDto.setKey(authorKey);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        return authorMapper.mapTo(savedAuthorEntity);
    }

    @Override
    public AuthorDto getOrCreateOpenAuthor(String referenceKey) {
        String authorKey = extractAuthorKey(referenceKey);
        return authorRepository.findByKey(authorKey)
                .map(authorMapper::mapTo)
                .orElseGet(() -> createOpenAuthor(authorKey));
    }

    private String extractAuthorKey(String referenceKey) {
        if (referenceKey.startsWith("/authors/")) {
            return referenceKey.substring("/authors/".length());
        }

        return referenceKey;
    }

    @Override
    public List<AuthorDto> findAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::mapTo)
                .toList();
    }

    private AuthorEntity getAuthorEntityOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
    }

    @Override
    public AuthorDto findOneOrThrow(Long id) {
        AuthorEntity authorEntity = getAuthorEntityOrThrow(id);

        return authorMapper.mapTo(authorEntity);
    }

    @Override
    public AuthorDto updateFullAuthor(Long id, AuthorDto authorDto) {
        AuthorEntity existingAuthorEntity = getAuthorEntityOrThrow(id);
        authorMapper.updateFullAuthorFromDto(authorDto, existingAuthorEntity);
        AuthorEntity savedAuthorEntity = authorRepository.save(existingAuthorEntity);

        return authorMapper.mapTo(savedAuthorEntity);
    }

    @Override
    public AuthorDto updatePartialAuthor(Long id, AuthorDto authorDto) {
        AuthorEntity existingAuthorEntity = getAuthorEntityOrThrow(id);
        authorMapper.updatePartialAuthorFromDto(authorDto, existingAuthorEntity);
        AuthorEntity savedAuthorEntity = authorRepository.save(existingAuthorEntity);

        return authorMapper.mapTo(savedAuthorEntity);
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
