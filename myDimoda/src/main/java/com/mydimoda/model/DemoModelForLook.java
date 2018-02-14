package com.mydimoda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth on 2/13/2018.
 */

public class DemoModelForLook {
    private List list = new ArrayList();
    private boolean isLiked, isColsed;

    public DemoModelForLook(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isColsed() {
        return isColsed;
    }

    public void setColsed(boolean colsed) {
        isColsed = colsed;
    }
}
