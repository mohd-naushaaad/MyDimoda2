package com.mydimoda.model;

public class DialogImagesModel {
    String imagePathl;
    long origId;
    boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getImagePathl() {
        return imagePathl;
    }

    public void setImagePathl(String imagePathl) {
        this.imagePathl = imagePathl;
    }

    public long getOrigId() {
        return origId;
    }

    public void setOrigId(long origId) {
        this.origId = origId;
    }


}
