package com.github.tonywills.xkcdviewer.api.model;

import android.graphics.Bitmap;

public class Comic {

    private String title;
    private String alt;
    private String img;
    private int num;

    private boolean favourite;
    private String localCopyPath;
    private transient Bitmap localCopy;

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

    public String getLocalCopyPath() {
        return localCopyPath;
    }

    public void setLocalCopyPath(String localCopyPath) {
        this.localCopyPath = localCopyPath;
    }

    public Bitmap getLocalCopy() {
        return localCopy;
    }

    public void setLocalCopy(Bitmap localCopy) {
        this.localCopy = localCopy;
    }
}
