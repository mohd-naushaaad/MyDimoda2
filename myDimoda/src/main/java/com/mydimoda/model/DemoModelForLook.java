package com.mydimoda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth on 2/13/2018.
 */

public class DemoModelForLook {
    private List list = new ArrayList();

    public DemoModelForLook(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }
}
