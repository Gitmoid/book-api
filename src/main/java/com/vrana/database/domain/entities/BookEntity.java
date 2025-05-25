package com.vrana.database.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    private String isbn;

    private String key;
    private String title;
    private String subtitle;
    private List<String> isbn10;
    private Integer pageNumber;
    private String publishDate;
    private String publishCountry;
    private List<String> publishPlaces;
    private List<String> publishers;
    private List<String> subjects;
    private List<String> contributions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AuthorEntity author;
}
