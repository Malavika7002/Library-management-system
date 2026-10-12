package com.library.student.model;

import java.sql.Date;

public class BorrowRecord {
    private int borrowId;
    private String admissionNo;
    private int bookId;
    private String bookTitle;
    private Date borrowDate;
    private Date dueDate;
    private int renewalCount;
    private String status;

    public BorrowRecord(int borrowId, String admissionNo, int bookId, String bookTitle, Date borrowDate, Date dueDate, int renewalCount, String status) {
        this.borrowId = borrowId;
        this.admissionNo = admissionNo;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.renewalCount = renewalCount;
        this.status = status;
    }

    // Getters
    public int getBorrowId() { return borrowId; }
    public String getStudentId() { return admissionNo; }
    public int getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public Date getBorrowDate() { return borrowDate; }
    public Date getDueDate() { return dueDate; }
    public int getRenewalCount() { return renewalCount; }
    public String getStatus() { return status; }
}