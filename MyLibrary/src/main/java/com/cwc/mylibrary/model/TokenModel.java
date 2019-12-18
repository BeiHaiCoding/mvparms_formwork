package com.cwc.mylibrary.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/16.
 */
public class TokenModel implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "token='" + token + '\'' +
                '}';
    }
}
