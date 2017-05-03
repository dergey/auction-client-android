package com.zhuravlev.sergey.remindme.dto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class LotDTO {

    private String title, description;
    private Double price;
    private String currency = "BYN";
    private String image;

    public LotDTO(String title, String description, Double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public LotDTO(String title, String description, String image, Double price) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Bitmap getImage(){
        if (image != null) {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        }
        return null;
    }

}