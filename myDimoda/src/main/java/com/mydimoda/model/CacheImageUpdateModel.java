package com.mydimoda.model;

import java.util.Observable;

/**
 * Created by Mayur on 06-04-17.
 */

public class CacheImageUpdateModel extends Observable {

    boolean isUploaded;
    int pos;

    public CacheImageUpdateModel(boolean isUploaded, int pos) {
        this.isUploaded = isUploaded;
        this.pos = pos;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
