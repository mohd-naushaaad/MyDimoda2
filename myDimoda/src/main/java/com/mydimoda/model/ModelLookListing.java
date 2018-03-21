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

    public ModelLookListing(List<OrderClothModel> listOfCloth, String clothType, String mode) {
        this.listOfCloth = listOfCloth;
        this.clothType = clothType;
        this.mode = mode;
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
