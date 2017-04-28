package com.example.bookstorelocaldemo;

import java.util.List;

public class SearchBook {

    private String id;
    private String title;
    private String image;
    private List<String> author;

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthor() {
        return author;
    }
}
