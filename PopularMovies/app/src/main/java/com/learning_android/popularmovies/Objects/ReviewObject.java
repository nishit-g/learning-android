package com.learning_android.popularmovies.Objects;

public class ReviewObject {
    private String author;
    private String content;

    // Empty Constructor
    public ReviewObject(){ }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
