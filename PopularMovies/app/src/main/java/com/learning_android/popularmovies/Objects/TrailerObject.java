package com.learning_android.popularmovies.Objects;

public class TrailerObject {
    private String videoKey;
    public TrailerObject(){}

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    @Override
    public String toString() {
        return "TrailerObject{" +
                "videoKey='" + videoKey + '\'' +
                '}';
    }
}
