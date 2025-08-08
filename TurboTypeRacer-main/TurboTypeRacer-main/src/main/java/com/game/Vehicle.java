package com.game;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String name;
    private String imageUrl;

    public Vehicle(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
