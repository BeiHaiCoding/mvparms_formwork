package com.cwc.mylibrary.base;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cwc.mylibrary.Log.MLogHelper;
import com.cwc.mylibrary.takephoto.app.TakePhoto;
import com.cwc.mylibrary.takephoto.app.TakePhotoFragment;
import com.cwc.mylibrary.takephoto.compress.CompressConfig;
import com.cwc.mylibrary.takephoto.model.CropOptions;
import com.cwc.mylibrary.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BaseFragment extends TakePhotoFragment {
    protected String TAG = this.getClass().getSimpleName();
    public View view;
    public Activity mActivity;
    public Context mContext;
    private TakePhoto takePhoto;
    private CropOptions config_crop;
    private CompressConfig config_compress;
    private TakePhotoOptions config_takePhoto;
    protected boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(setLayoutId(), container, false);
        } else {
            //  缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        MLogHelper.i("BaseFragment", TAG + " --- onCreateView执行了");

        mActivity = getActivity();
        mContext = getContext();

        initView();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    private void onInVisible() {
    }

    private void onVisible() {
        lazyLoad();
    }


    public abstract int setLayoutId();

    public abstract void initView();

    /**
     * 关闭预加载，fragment可见时调用方法，该方法在onCreateView（）之前调用
     */
    protected abstract void lazyLoad();

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
                .setMaxPixel(800)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config_compress, true);
    }

    public void configCrop() {
        config_crop = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
    }

    /**
     * 调用摄像头拍照
     *
     * @param fileName
     */
    public void takePhoto(String fileName) {
        initTakePhotoConfig();
        File file = new File(Environment.getExternalStorageDirectory(), getActivity().getApplicationInfo().packageName + "/" + fileName + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        takePhoto.onPickFromCaptureWithCrop(Uri.fromFile(file), config_crop);

    }

    /**
     * 调用takePhoto图库选择图片
     *
     * @param PicCount
     */
    public void selectPhoto(int PicCount) {
        initTakePhotoConfig();
        takePhoto.onPickMultipleWithCrop(PicCount, config_crop);
    }

    public View findViewById(int viewId) {
        return view.findViewById(viewId);
    }
}
