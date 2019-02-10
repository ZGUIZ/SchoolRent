package com.example.amia.schoolrent.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class NetBitmap implements Serializable {
    private String id;
    private Bitmap bitmap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
