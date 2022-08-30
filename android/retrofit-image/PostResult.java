package com.lkwoung.image_sender_json;

import com.google.gson.annotations.SerializedName;

public class PostResult {
    @SerializedName("image")
    private int state;
    private String message;

    public PostResult(int state, String message) {
        this.state = state;
        this.message = message;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "state='" + state + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
