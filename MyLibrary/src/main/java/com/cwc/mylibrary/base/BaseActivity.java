package com.cwc.mylibrary.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cwc.mylibrary.R;
import com.cwc.mylibrary.Toast.MToastHelper;
import com.cwc.mylibrary.app.AppMgr;
import com.cwc.mylibrary.contans.Constans;
import com.cwc.mylibrary.takephoto.app.TakePhotoFragmentActivity;
import com.cwc.mylibrary.utils.NetHelper;
import com.cwc.mylibrary.utils.ScreenHelper;
import com.cwc.mylibrary.takephoto.app.TakePhoto;

import com.cwc.mylibrary.takephoto.compress.CompressConfig;
import com.cwc.mylibrary.takephoto.model.CropOptions;
import com.cwc.mylibrary.takephoto.model.TakePhotoOptions;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;


public abstract class BaseActivity extends TakePhotoFragmentActivity {
    protected String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    protected Activity mActivity;
    protected View titleBar;

    protected LinearLayout ll_titleBar;

    protected LinearLayout ll_title_bar_left;
    protected ImageView left_icon;
    protected TextView left_text;

    protected LinearLayout ll_title_bar_middle;
    protected ImageView middle_icon;
    protected TextView middle_text1;
    protected View middle_view;
    protected TextView middle_text2;
    protected EditText middle_edit;

    protected LinearLayout ll_title_bar_right;
    protected ImageView right_icon;
    protected TextView right_text;

    protected FrameLayout fl_title_bar;
    protected FrameLayout fl_content;
    protected boolean isBack;
    /**
     * 是否沉浸状态栏
     **/
    public static boolean isSetStatusBar = false;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = true;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;
    /**
     * 是否打开网络监听
     **/
    public boolean isAllowNet = false;

    private boolean isNetConnected = false;
    //是wifi连接还是手机连接
    private boolean isWiFiConnected = false;


    //检测网络连接状态
    private ConnectivityManager manager;

    NetCheckReceiver ncReceiver;

    private TakePhoto takePhoto;
    private CropOptions config_crop;
    private CompressConfig config_compress;
    private TakePhotoOptions config_takePhoto;

    // Activity创建时被调用
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
        }
        if (isSetStatusBar) {
            steepStatusBar();
        }
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//水平
        }

        ready();

        // 获取基础视图(titleBar视图)
        setContentView(R.layout.act_base);

        AppMgr.getInstance().addAct(this); //将activity加入AppManager

        mContext = this;
        mActivity = this;
        //初始化takePhoto参数
//        initTakePhotoConfig();

        // 初始化所有Base控件
        initBaseViews();

        //初始化content内容和具体业务操作
        initViews();
        if (isAllowNet) {  //是否打开网络监听
            //注册网络状态监听
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            ncReceiver = new NetCheckReceiver();
            registerReceiver(ncReceiver, intentFilter);
        }

    }

    public void initTakePhotoConfig() {
        takePhoto = getTakePhoto();
        //配置剪切和压缩参数
        configTakePhoto();
        configCrop();
        configCompress();
    }

    public void configTakePhoto() {
        config_takePhoto = new TakePhotoOptions.Builder().setWithOwnGallery(true).create();
        takePhoto.setTakePhotoOptions(config_takePhoto);
    }

    public void configCompress() {
        config_compress = new CompressConfig.Builder()
                .setMaxSize(102400)
                .setMaxPixel(1960)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config_compress, true);
    }

    public void configCrop() {
        config_crop = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
    }

    class NetCheckReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetHelper.checkNetState(mActivity)) {
                isNetConnected = true;

                int net_type = NetHelper.getNetType();
                if (Constans.NET_TYPE_MOBILE == net_type) {  //手机连接
                    isWiFiConnected = false;
                    MToastHelper.sucToast(mActivity, "当前为非wifi状态，请注意流量使用", 2000);
                } else if (Constans.NET_TYPE_WIFI == net_type) {  //wifi连接
                    isWiFiConnected = true;
//                    MToastHelper.sucToast(mActivity, "当前为wifi状态", 2000);
                }

            } else {

                isNetConnected = false;
                MToastHelper.errToast(mActivity, "当前无网络，请检查连接！", 2000);

            }
        }
    }

    /**
     * 调用摄像头拍照带剪切
     *
     * @param fileName
     */
    public void takePhoto(String fileName) {
        initTakePhotoConfig();
        File file = new File(Environment.getExternalStorageDirectory(), getApplicationInfo().packageName + "/" + fileName + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        takePhoto.onPickFromCaptureWithCrop(Uri.fromFile(file), config_crop);
    }

    public void takePhotoWithOutCrop(String fileName) {
        initTakePhotoConfig();
        File file = new File(Environment.getExternalStorageDirectory(), getApplicationInfo().packageName + "/" + fileName + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        takePhoto.onPickFromCapture(Uri.fromFile(file));
    }
    /**
     * 调用takePhoto图库选择图片
     *
     * @param PicCount 选择张数
     */
    public void selectPhoto(int PicCount) {
        initTakePhotoConfig();
        takePhoto.onPickMultipleWithCrop(PicCount, config_crop);
    }
    public void selectPhotoWithOutCrop(int PicCount) {
        initTakePhotoConfig();
        takePhoto.onPickMultiple(PicCount);
    }
    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 进入activity之前的准备或者判断
     */
    protected void ready() {

    }



    /**
     * 将BaseActivity的控件都初始化
     */
    public void initBaseViews() {
        //获取titleBar下面的layout资源(titlebar的根布局)
        fl_title_bar = (FrameLayout) findViewById(R.id.fl_title_bar);
        ViewGroup.LayoutParams titBarP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenHelper.sp2px(this, 50));

        if (loadTitleBarXml() == 0) {  //表示titlebar使用默认的
            titleBar = LayoutInflater.from(this).inflate(R.layout.title_bar, null);
            fl_title_bar.addView(titleBar, titBarP);

            //执行findViewById(...)和加载content资源文件
            ll_titleBar = (LinearLayout) titleBar.findViewById(R.id.titleBar);
            ll_titleBar.setBackgroundColor(ActivityCompat.getColor(mActivity, R.color.type_blue1));
            ll_title_bar_left = (LinearLayout) titleBar.findViewById(R.id.ll_title_bar_left);
            left_icon = (ImageView) titleBar.findViewById(R.id.left_icon);
            left_text = (TextView) titleBar.findViewById(R.id.left_text);

            ll_title_bar_middle = (LinearLayout) titleBar.findViewById(R.id.ll_title_bar_middle);
            middle_icon = (ImageView) titleBar.findViewById(R.id.middle_icon);
            middle_text1 = (TextView) titleBar.findViewById(R.id.middle_text1);
            middle_view = titleBar.findViewById(R.id.middle_view);
            middle_text2 = (TextView) titleBar.findViewById(R.id.middle_text2);
            middle_edit = (EditText) titleBar.findViewById(R.id.middle_edit);

            ll_title_bar_right = (LinearLayout) titleBar.findViewById(R.id.ll_title_bar_right);
            right_icon = (ImageView) titleBar.findViewById(R.id.right_icon);
            right_text = (TextView) titleBar.findViewById(R.id.right_text);

            initTitleBar();

        } else {  //表示titlebar使用自定义的
            titleBar = LayoutInflater.from(this).inflate(loadTitleBarXml(), null);
            fl_title_bar.addView(titleBar, titBarP);
            initTitleBar();
        }
        // 将子视图加载到目视图中
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        //获取titleBar下面的layout资源
        View childView = LayoutInflater.from(this).inflate(loadContentXml(), null);

        fl_content.addView(childView, layoutParams);
    }

    /**
     * 初始化TitleBar内容 要隐藏titleBar 找 fl_title_bar
     */
    protected abstract void initTitleBar();

    /**
     * 加载TitleBar资源文件(如返回0则默认加载titlebar.xml)
     *
     * @return
     */
    protected abstract int loadTitleBarXml();

    /**
     * 加载content资源文件
     *
     * @return
     */
    protected abstract int loadContentXml();

    /**
     * 初始化控件和具体业务操作
     */
    protected abstract void initViews();

    /**
     * 隐藏菜单键
     */
    protected void hideSetting() {
    }


    // Activity创建或者从后台重新回到前台时被调用
    @Override
    protected void onStart() {
        super.onStart();
    }

    // Activity从后台重新回到前台时被调用
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // Activity创建或者从被覆盖、后台重新回到前台时被调用
    @Override
    protected void onResume() {
        super.onResume();
    }

    // Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后
    /*
     * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus); Log.i(TAG,
	 * "onWindowFocusChanged called."); }
	 */

    // Activity被覆盖到下面或者锁屏时被调用
    @Override
    protected void onPause() {
        super.onPause();
        // 有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据

    }

    // 退出当前Activity或者跳转到新Activity时被调用
    @Override
    protected void onStop() {
        super.onStop();
    }

    // 退出当前Activity时被调用,调用之后Activity就结束了
    @Override
    protected void onDestroy() {
        if (ncReceiver != null) {
            unregisterReceiver(ncReceiver);
        }
        super.onDestroy();
        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    /**
     * Activity被系统杀死时被调用. 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态. 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //对返回键进行修改
    private long[] mHits = new long[2];
    protected boolean isOpen2Back;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isOpen2Back && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();

            //2s内连续按2次返回键
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
                AppMgr.getInstance().closeAllActs();
            } else {
                MToastHelper.sucToast(this, "再按一次退出", 2000);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }



}
