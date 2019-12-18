package com.cwc.mylibrary.Log;


import com.cwc.mylibrary.model.LogModel;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;


/**
 * Created by Administrator on 2017/3/31.
 */

public class MLogHelper {

    static {
        Logger.init("MLogHelper").hideThreadInfo().methodCount(3).logLevel(LogLevel.FULL);
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 自己常用的
    public static void log(String tag, LogModel nameLM,LogModel valuLM) {
        if (isDebug) {
            String temp = "";
            for (int i = 0; i < nameLM.getmStrings().size(); ++i) {
                String name = nameLM.getmStrings().get(i);
                String value = valuLM.getmStrings().get(i);
                //名字
                if (i == 0) {
                    name = name;
                } else {
                    name = "\n" + name;
                }

                temp = temp + name + " = " + value;
            }
            Logger.t(tag).i(temp);

        }

    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Logger.i(msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Logger.d(msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Logger.e(msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Logger.v(msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).i(msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).d(msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).e(msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).v(msg);
    }

    public static void json(String json){
        if (isDebug)
            Logger.json(json);
    }

    public static void json(String tag,String json){
        if (isDebug)
            Logger.t(tag).json(json);
    }

}
