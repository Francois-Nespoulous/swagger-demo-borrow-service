package com.example.borrow.domain.model;

import java.util.List;
import java.util.UUID;

public class BookDefinition {
    private UUID id;

    private String title;
    private String author;

    private int nbTotalBooks;
    private int nbBorrowedBooks;

    private List<BookInstance> bookInstances;

    public BookDefinition(UUID id, String title, String author, List<BookInstance> bookInstances, int nbTotalBooks, int nbBorrowedBooks) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.nbTotalBooks = nbTotalBooks;
        this.nbBorrowedBooks = nbBorrowedBooks;
        this.bookInstances = bookInstances;
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

    public int getNbTotalBooks() {
        return nbTotalBooks;
    }

    public int getNbBorrowedBooks() {
        return nbBorrowedBooks;
    }

    public List<BookInstance> getBookInstances() {
        return bookInstances;
    }


    public boolean isAvailable() {
        return getNbBorrowedBooks() < getNbTotalBooks();
    }

    public boolean isBorrowed() {
        return getNbBorrowedBooks() > 0;
    }
}
