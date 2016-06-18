package com.github.tonywills.xkcdviewer.api.model;

import android.media.Image;

public class Comic {

    private String title;
    private String alt;
    private String img;
    private int num;

    private Image loadedImage;
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

    public Image getLoadedImage() {
        return loadedImage;
    }

    public void setLoadedImage(Image loadedImage) {
        this.loadedImage = loadedImage;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
