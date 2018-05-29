package com.mydimoda.model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth on 2/27/2018.
 */
@Parcel
public class ModelLookListing {
    @SerializedName("listOfCloth")
    public List<OrderClothModel> listOfCloth = new ArrayList();
    public String clothType = "";
    public String mode = "";
    private boolean isliked = false;

    public boolean isIsliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }

    public ModelLookListing(List<OrderClothModel> listOfCloth, String clothType, String mode, boolean isliked) {
        this.listOfCloth = listOfCloth;
        this.clothType = clothType;
        this.mode = mode;
        this.isliked = isliked;
    }

    public ModelLookListing() {
    }

    public List<OrderClothModel> getList() {
        return listOfCloth;
    }

    public void setListOfCloth(List<OrderClothModel> listOfCloth) {
        this.listOfCloth = listOfCloth;
    }

    public String getClothType() {
        return clothType;
    }

    public void setClothType(String clothType) {
        this.clothType = clothType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
