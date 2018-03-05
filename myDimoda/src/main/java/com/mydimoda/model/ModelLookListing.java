package com.mydimoda.model;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth on 2/27/2018.
 */

public class ModelLookListing {
    private List<OrderClothModel> list = new ArrayList();
    private String clothType = "";
    private int colorCode;

    public ModelLookListing(List<OrderClothModel> list, String clothType, int colorCode) {
        this.list = list;
        this.clothType = clothType;
        this.colorCode = colorCode;
    }


    public String getClothType() {
        return clothType;
    }

    public int getColorCode() {
        return colorCode;
    }

    public List<OrderClothModel> getList() {
        return list;
    }

    public void setList(List<OrderClothModel> list) {
        this.list = list;
    }
}
