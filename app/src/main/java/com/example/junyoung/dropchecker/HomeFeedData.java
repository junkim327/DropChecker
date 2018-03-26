package com.example.junyoung.dropchecker;

import java.util.HashMap;

public class HomeFeedData {
    private String title;
    private String imageUrl;
    private String releaseDate;
    private HashMap<String, Boolean> usersWhoLiked;

    HomeFeedData() {}

    public String getTitle() {
        return title;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public HashMap<String, Boolean> getUsersWhoLiked() {
        return usersWhoLiked;
    }
}
