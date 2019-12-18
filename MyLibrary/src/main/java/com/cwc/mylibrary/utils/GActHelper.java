package com.cwc.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by ASUS on 2016/1/18.activity跳转页面相关帮助类
 */
public class GActHelper {

    /**
     * 界面跳转
     *
     * @param cls
     */
    public static void startAct(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 界面跳转
     *
     * @param cls
     */
    public static void startAct(Context context, Class<?> cls, Serializable obj) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("message",obj);
        context.startActivity(intent);
    }
    /**
     * 界面跳转
     * 带string参数
     *
     * @param cls
     */
    public static void startAct(Context context, Class<?> cls, String msg) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra("message", msg);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 界面跳转
     * 带string参数
     *
     * @param cls
     */
    public static void startAct(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra("message", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 界面跳转
     * 带一个string字符集合
     *
     * @param cls
     */
    public static void startAct(Context context, Class<?> cls, ArrayList<String> list) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putStringArrayListExtra("message", list);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 界面跳转  //其他模式可能造成 onActivityResult 方法无响应
     *
     * @param cls
     */
    public static void startActForResult(Context context, Class<?> cls, int requestCode) {
        Intent intent = new Intent(context, cls);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
    /**
     * 界面跳转
     *
     * @param cls
     */
    public static void startActForResult(Context context, Class<?> cls, int requestCode,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra("message", bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 界面跳转
     * 带string参数
     *
     * @param cls
     */
    public static void startActForResult(Context context, Class<?> cls, int requestCode, String msg) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("message", msg);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
    /**
     * 界面跳转
     * 带一个string字符集合
     *
     * @param cls
     */
    public static void startActForResult(Context context, Class<?> cls, int requestCode, ArrayList<String> list) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putStringArrayListExtra("message", list);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}
