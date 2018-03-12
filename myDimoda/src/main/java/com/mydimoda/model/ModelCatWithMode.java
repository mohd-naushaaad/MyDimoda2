package com.mydimoda.model;

/**
 * Created by hp1 on 3/12/2018.
 */

public class ModelCatWithMode {
    private String category;
    private String mode;

    public ModelCatWithMode(String category, String mode) {
        this.category = category;
        this.mode = mode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
