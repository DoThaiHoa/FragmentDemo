package com.chancahhoop.release.voicesearchanything;

import android.graphics.drawable.Drawable;

/**
 * Created by hoadt on 18/12/2017.
 */

public class QuanLy {
    private String name;
    public String packageName;
    private Drawable image;

    public QuanLy() {
    }

    public QuanLy(String name,Drawable image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
