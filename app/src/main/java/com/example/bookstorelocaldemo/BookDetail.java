package com.example.bookstorelocaldemo;

import org.litepal.crud.DataSupport;

import java.util.List;


public class BookDetail extends DataSupport{

    private String bookid;
    private String username;
    private String title;
    private String pubdate;
    private String price;
    private String summary;
    private String image;
    private List<String> author;

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBookid() {
        return bookid;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getSummary() {
        return summary;
    }
}
