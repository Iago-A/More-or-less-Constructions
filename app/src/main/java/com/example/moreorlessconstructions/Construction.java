package com.example.moreorlessconstructions;

public class Construction {
    private String name;
    private int height;
    private String image;
    private String country;

    public Construction() {
    }

    public Construction(int height, String image, String country, String name) {
        this.height = height;
        this.image = image;
        this.country = country;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
