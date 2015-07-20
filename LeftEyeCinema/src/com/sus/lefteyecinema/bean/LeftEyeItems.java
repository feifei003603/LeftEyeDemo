
package com.sus.lefteyecinema.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class LeftEyeItems implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;


    private ArrayList<LeftEyeItem> items = new ArrayList<LeftEyeItem>();


    public ArrayList<LeftEyeItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<LeftEyeItem> items) {
        this.items = items;
    }

    public void addItem(LeftEyeItem item) {
        items.add(item);
    }
}
