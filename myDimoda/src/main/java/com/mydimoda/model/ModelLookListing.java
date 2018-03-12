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

    public ModelLookListing(List<OrderClothModel> listOfCloth, String clothType) {
        this.listOfCloth = listOfCloth;
        this.clothType = clothType;
    }

    public ModelLookListing() {
    }

    public String getClothType() {
        return clothType;
    }

    public List<OrderClothModel> getList() {
        return listOfCloth;
    }

    public void setList(List<OrderClothModel> listOfCloth) {
        this.listOfCloth = listOfCloth;
    }
}
