package com.example.borrow.persistence.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "book_definition")
public class BookDefinitionEntity {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private UUID id;

    private String title;
    private String author;

    @OneToMany(mappedBy = "bookDefinition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookInstanceEntity> bookInstances = new ArrayList<>();

    public BookDefinitionEntity() {
    }

    public BookDefinitionEntity(UUID id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<BookInstanceEntity> getBookInstances() {
        return bookInstances;
    }

    public void addBookInstance(BookInstanceEntity bookInstanceEntity) {
        bookInstances.add(bookInstanceEntity);
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
