package com.mydimoda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth on 2/13/2018.
 */

public class DemoModelForLook {
    private List list = new ArrayList();

    public List getList() {
        for (int i = 0; i < 2; i++) {
            list.add(i);
        }
        return list;
    }

}
