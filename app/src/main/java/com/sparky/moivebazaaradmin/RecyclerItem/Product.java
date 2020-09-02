package com.sparky.moivebazaaradmin.RecyclerItem;

/**
 * Created by Belal on 10/18/2017.
 */


public class Product {
    private String title;
    private String imageUrl;
    private String videoUrl;


    public Product( String title, String imageUrl, String videoUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}

