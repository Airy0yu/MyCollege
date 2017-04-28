package com.example.bookstorelocaldemo;

import java.util.List;

public class SearchBookStore {

    private List<SearchBook> books;
    private int count;

    public int getCount() {
        return count;
    }

    public List<SearchBook> getBooks() {
        return books;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBooks(List<SearchBook> books) {
        this.books = books;
    }
}
