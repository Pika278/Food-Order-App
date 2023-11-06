package com.example.foodorderapp.model;

import java.io.Serializable;
import java.util.List;

public class Food implements Serializable {

    private long id;
    private String name;
    private String description;
    private int price;
    private int sale;
    private String image;
    private String banner;
//    private boolean popular;
    private List<Image> images;

    public Food() {
    }

    public Food(long id, String name, String description, int price, int sale,
                      String image, String banner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sale = sale;
        this.image = image;
        this.banner = banner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
