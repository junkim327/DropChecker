package com.example.junyoung.dropchecker;

public class Post {
    private String name;
    private String size;
    private String price;
    private String subject;
    private String content;
    private String userPhotoUrlString;

    public Post() {

    }

    public Post(String name, String size, String price, String subject, String content,
                String userPhotoUrl) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.subject = subject;
        this.content = content;
        this.userPhotoUrlString = userPhotoUrl;
    }

    public String getName() {
        return name;
    }

    public String getSize() { return size; }

    public String getPrice() { return price; }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getUserPhotoUrlString() { return userPhotoUrlString; }
}
