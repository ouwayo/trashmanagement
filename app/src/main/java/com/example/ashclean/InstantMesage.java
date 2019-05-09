package com.example.ashclean;

public class InstantMesage {
    private String message;
    private String author;


    public InstantMesage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    //Requirement by firebase

    //No argument constructor
    public InstantMesage() {

    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
