package com.cwc.mylibrary.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ResultModel implements Serializable {
    private Object data;
    private PageModel pagination;
    private int tag;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public PageModel getPagination() {
        return pagination;
    }

    public void setPagination(PageModel pagination) {
        this.pagination = pagination;
    }


    @Override
    public String toString() {
        return "ResultModel{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}
