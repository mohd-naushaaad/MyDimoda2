package com.mydimoda.model;


import java.util.ArrayList;
import java.util.List;

public class ModelForBlockedStyle {

    public List<List<ItemsData>> casual = new ArrayList<>();
    public List<List<ItemsData>> formal = new ArrayList<>();
    public List<List<ItemsData>> after5 = new ArrayList<>();

    public static class ItemsData {
        public String type;
        public String id;
    }

    public List<List<ItemsData>> getCasual() {
        return casual;
    }

    public void setCasual(List<List<ItemsData>> casual) {
        this.casual = casual;
    }

    public List<List<ItemsData>> getFormal() {
        return formal;
    }

    public void setFormal(List<List<ItemsData>> formal) {
        this.formal = formal;
    }

    public List<List<ItemsData>> getAfter5() {
        return after5;
    }

    public void setAfter5(List<List<ItemsData>> after5) {
        this.after5 = after5;
    }
}
