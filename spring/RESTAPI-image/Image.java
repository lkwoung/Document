package com.lkwoung.imageserverjson.demo;

public class Image {
    String message;

    Image() {
        message = "ok";
    }

    @Override
    public String toString() {
        return "Image{" +
                "message='" + message + '\'' +
                '}';
    }

}
