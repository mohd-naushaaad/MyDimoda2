package com.mydimoda.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth on 3/5/2018.
 */

public class TripLookForInsetinParse {
    public TripLookForInsetinParse() {
    }

    String clothType = "";

    public String getClothType() {
        return clothType;
    }

    public void setClothType(String clothType) {
        this.clothType = clothType;
    }

    public List<ClothDetails> getListOfCloth() {
        return listOfCloth;
    }

    public void setListOfCloth(List<ClothDetails> listOfCloth) {
        this.listOfCloth = listOfCloth;
    }

    List<ClothDetails> listOfCloth = new ArrayList<>();

}
