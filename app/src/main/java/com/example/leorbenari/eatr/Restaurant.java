package com.example.leorbenari.eatr;


import java.util.List;

public class Restaurant {

    private String name;
    private String businessURL;
    private String picURL;
    private List<String> pictures;

    private String ratingCategory;

    public Restaurant(String name, String businessURL) {
        this.name = name;
        setBusinessURL(businessURL);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessURL() {
        return businessURL;
    }

    public void setBusinessURL(String businessURL) {
        this.businessURL = businessURL;
        this.picURL = businessURL.replace("/biz/", "/biz_photos/");
        this.picURL += "?tab=food";
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getRatingCategory() {
        return ratingCategory;
    }

    public void setRatingCategory(String ratingCategory) {
        this.ratingCategory = ratingCategory;
    }
}
