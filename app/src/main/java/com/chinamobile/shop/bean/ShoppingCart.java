package com.chinamobile.shop.bean;

import java.io.Serializable;

/**
 * Created by yjj on 2017/2/4.
 */

public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "count=" + count +
                ", isChecked=" + isChecked +
                '}';
    }
}
