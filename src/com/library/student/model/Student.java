package com.library.student.model;

public class Student {
    private String admissionNo;
    private String name;
    private String email;
    private String phoneNumber;

    public Student(String admissionNo, String name, String email, String phoneNumber) {
        this.admissionNo = admissionNo;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}