package com.cwc.mylibrary.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/16.
 */
public class PageModel implements Serializable {
    private int total_row;//总个数
    private int total_page;//总页数
    private int current_page;//当前页


    public int getTotal_row() {
        return total_row;
    }

    public void setTotal_row(int total_row) {
        this.total_row = total_row;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @Override
    public String toString() {
        return "PageModel{" +
                "total_row='" + total_row + '\'' +
                ", total_page=" + total_page +
                ", current_page=" + current_page +
                '}';
    }
}
