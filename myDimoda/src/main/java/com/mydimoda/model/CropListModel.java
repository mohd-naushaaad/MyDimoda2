package com.mydimoda.model;

import android.graphics.Bitmap;

/**
 * Created by Mainank on 16-03-2016.
 */
public class CropListModel {

    private String mColor="";
    private String mPattern="";
    private Bitmap mImage;
    private String mType="";
    private String mCategory="";
    private int position;
    private boolean isError;

    public boolean isError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmPattern() {
        return mPattern;
    }

    public void setmPattern(String mPattern) {
        this.mPattern = mPattern;
    }

    public Bitmap getmImage() {
        return mImage;
    }

    public void setmImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
