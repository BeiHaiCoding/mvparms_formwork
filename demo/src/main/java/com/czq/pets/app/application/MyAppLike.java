package com.czq.pets.app.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import java.util.Locale;


/**
 * Created by MarkFrank01
 * description :
 */
public class MyAppLike extends DefaultApplicationLike {

    public static final String TAG = "Tinker.MyAppLike";

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    //全局Context
    public static Context mContext = null;


    public MyAppLike(Application application, int tinkerFlags,
                     boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                     long applicationStartMillisTime, Intent tinkerResultIntent) {

        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplication();

        setStrictMode();

        //设置是否初始化时自动检测更新
        Beta.autoCheckUpgrade = true;
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = false;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        /**
         *  全量升级状态回调
         */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                Log.i(TAG,"onUpgradeFailed");
            }

            @Override
            public void onUpgradeSuccess(boolean b) {
                Log.i(TAG,"onUpgradeSuccess");
            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                Log.i(TAG,"最新版本");
            }

            @Override
            public void onUpgrading(boolean b) {
                Log.i(TAG,"onUpgrading");
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Log.i(TAG,"onDownloadCompleted");
            }
        };

        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                Log.i(TAG,patchFileUrl);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Log.i(TAG, String.format(Locale.getDefault(),
                        "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                Log.i(TAG,patchFilePath);
            }

            @Override
            public void onDownloadFailure(String msg) {
                Log.i(TAG,msg);
            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(getApplication(),"热修复成功，请退出App并在后台任务里关闭应用后重新打开App",Toast.LENGTH_LONG).show();
                Log.i(TAG,msg);
            }

            @Override
            public void onApplyFailure(String msg) {
                Log.i(TAG,msg);
            }

            @Override
            public void onPatchRollback() {
                Log.i(TAG,"onPatchRollback");
            }
        };

        // 这里实现Bugly初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(getApplication(), "f16a078684", true);

    }



    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        Beta.installTinker(this);

    }

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }




}
