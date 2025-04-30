package com.vrana.database.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrana.database.TestDataUtil;
import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private AuthorService authorService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String testAuthorDtoAJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorDtoAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String testAuthorDtoAJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorDtoAJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        AuthorDto testAuthorDtoB = TestDataUtil.createTestAuthorDtoB();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + testAuthorDtoA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedAuthor() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        AuthorDto testAuthorDtoB = TestDataUtil.createTestAuthorDtoB();
        testAuthorDtoB.setId(testAuthorDtoA.getId());
        String authorDtoUpdateJson = objectMapper.writeValueAsString(testAuthorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + testAuthorDtoA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testAuthorDtoA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDtoB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoB.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        AuthorDto testAuthorDtoB = TestDataUtil.createTestAuthorDtoB();
        testAuthorDtoB.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + testAuthorDtoA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsExistingAuthor() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        AuthorDto testAuthorDtoB = TestDataUtil.createTestAuthorDtoB();
        testAuthorDtoA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + testAuthorDtoA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testAuthorDtoA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoB.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForNonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception {
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + testAuthorDtoA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}