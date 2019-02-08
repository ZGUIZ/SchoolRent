package com.example.amia.schoolrent.Bean;

import android.graphics.Bitmap;

public class LocalPic {
    private boolean isAdd;
    private String localUri;
    private IdelPic pic;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getLocalUri() {
        return localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public IdelPic getPic() {
        return pic;
    }

    public void setPic(IdelPic pic) {
        this.pic = pic;
    }
}
