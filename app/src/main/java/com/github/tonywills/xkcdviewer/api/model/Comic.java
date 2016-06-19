package com.github.tonywills.xkcdviewer.api.model;

public class Comic {

    private String title;
    private String alt;
    private String img;
    private int num;

    private boolean favourite;

    public String getAlt() {
        return alt;
    }

    public String getImg() {
        return img;
    }

    public int getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

}
