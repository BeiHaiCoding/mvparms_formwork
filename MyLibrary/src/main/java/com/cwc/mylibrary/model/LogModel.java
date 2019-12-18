package com.cwc.mylibrary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class LogModel implements Serializable {
    List<String> mStrings = new ArrayList<>();

    public LogModel(String... strings) {
        for(String str :strings){
            mStrings.add(str);
        }
    }

    public List<String> getmStrings() {
        return mStrings;
    }

    public void setmStrings(List<String> mStrings) {
        this.mStrings = mStrings;
    }

    @Override
    public String toString() {
        return "LogModel{" +
                "mStrings=" + mStrings +
                '}';
    }
}
