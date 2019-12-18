//package com.cwc.mylibrary.base;
//
//import android.app.Application;
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import com.cwc.mylibrary.R;
//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
//import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//import com.nostra13.universalimageloader.utils.StorageUtils;
//
//import java.io.File;
//
//
///**
// * Created by Administrator on 2017/3/31.
// */
//
//public class BaseApplication extends Application {
//
//    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
//    static ImageLoader mLoader;
//    //全局Context
//    public static Context mContext = null;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mContext = getApplicationContext();
//
//        //初始化imageloader参数配置
//        initImageLoader();
//    }
//
//    /**
//     * 配置imageloader参数设置
//     */
//    public void initImageLoader() {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
//
//        // 初始化ImageLoader
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.waiting_loading)  //加载时候显示
//                .showImageForEmptyUri(R.drawable.waiting_loading) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.waiting_loading) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
//                .bitmapConfig(Bitmap.Config.RGB_565)  //默认是8888,565能降低内存
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//IN_SAMPLE_INT
////                .displayer(new FadeInBitmapDisplayer(200))  //图片显示淡入淡出
//                .build(); // 创建配置过得DisplayImageOption对象
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration
//                .Builder(getApplicationContext())
//                .diskCache(new UnlimitedDiskCache(cacheDir))  //自定义缓存路径
//                .defaultDisplayImageOptions(options)
//                .threadPriority(Thread.NORM_PRIORITY - 2)  //1-5个最佳
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .memoryCache(new WeakMemoryCache())
////                .memoryCache(new LruMemoryCache(10 * 1024 * 1024)) //建议内存设在5-10M,可以有比较好的表现
//                .build();
//        ImageLoader.getInstance().init(config);
//    }
//
//
//    /**
//     * "http://site.com/image.png" // from Web <br />
//     * "file:///mnt/sdcard/image.png" // from SD card<br />
//     * "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)<br />
//     * "content://media/external/images/media/13" // from content provider<br />
//     * "content://media/external/video/media/13" // from content provider (video thumbnail)<br />
//     * "assets://image.png" // from assets<br />
//     * "drawable://" + R.drawable.asimg // from drawables (non-9patch images)<br />
//     *
//     * @return
//     */
//    public static ImageLoader getImageLoader() {
//        if (mLoader == null) {
//            mLoader = ImageLoader.getInstance();
//            return mLoader;
//        }
//        return mLoader;
//    }
//
//}
