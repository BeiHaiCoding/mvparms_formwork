package com.cwc.mylibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cwc.mylibrary.contans.Constans;

/**
 * Created by Administrator on 2017/4/1.网络状态相关帮助类
 */

public class NetHelper {

    //检测网络连接状态
    private static ConnectivityManager manager;

    /**
     * 判断是否有网
     * @param context
     * @return
     */
    public static boolean checkNetState(Context context) {
        try {
            //得到网络连接信息
            manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                // 获取NetworkInfo对象
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                //去进行判断网络是否连接
                if (networkInfo != null) {
                    return networkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是mobile连接
     */
    public static int getNetType() {
        NetworkInfo.State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            return Constans.NET_TYPE_MOBILE;
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
            return Constans.NET_TYPE_WIFI;
        }
        return Constans.NET_TYPE_OTHER;
    }
}
