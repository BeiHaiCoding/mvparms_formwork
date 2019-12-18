package com.cwc.mylibrary.Toast;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.cwc.mylibrary.R;

import es.dmoral.toasty.Toasty;

/**
 * Created by Administrator on 2017/3/31.
 */

public class MToastHelper {
    public static int DEFAULT_SHORT_TIME = 1000;
    public static int DEFAULT_LONG_TIME = 2000;


    /**
     * 普通toast 时长1000
     *
     * @param context
     * @param cs
     */
    public static void showShort(Context context, CharSequence cs) {
        if (context != null && cs != null) {
            Toasty.normal(context, cs, DEFAULT_SHORT_TIME).show();

        }
    }


    /**
     * 普通toast 时长2000
     *
     * @param context
     * @param cs
     */
    public static void showLong(Context context, CharSequence cs) {
        if (context != null && cs != null) {
            Toasty.normal(context, cs, DEFAULT_LONG_TIME).show();
        }
    }

    /**
     * 普通toast 自定义时长
     *
     * @param context
     * @param cs
     * @param time
     */
    public static void customTime(Context context, CharSequence cs, int time) {
        if (context != null && cs != null) {
            Toasty.normal(context, cs, time).show();
        }
    }

    /**
     * 背景色为红色
     * @param context
     * @param cs
     * @param time
     */
    public static void errToast(Context context, CharSequence cs, int time) {
        if (context != null && cs != null) {
            Toasty.error(context, cs, time, false).show();
        }
    }

    /**
     * 背景色为绿色
     * @param context
     * @param cs
     * @param time
     */
    public static void sucToast(Context context, CharSequence cs, int time) {
        if (context != null && cs != null) {
            Toasty.success(context, cs, time, false).show();
        }
    }
}
