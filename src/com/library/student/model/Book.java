package com.library.student.model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private String type;
    private int availabilityStatus;

    public Book(int bookId, String title, String author, String category, String type, int availabilityStatus) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.type = type;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters
    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public int getAvailabilityStatus() { return availabilityStatus; }
}
