package com.cwc.mylibrary.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ErrModel implements Serializable {
    private int error_code; //错误码
    private String message; //错误信息

    public ErrModel(int error_code, String message) {
        this.error_code = error_code;
        this.message = message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrModel{" +
                "error_code=" + error_code +
                ", message='" + message + '\'' +
                '}';
    }

    private ErrModel errors;
}
