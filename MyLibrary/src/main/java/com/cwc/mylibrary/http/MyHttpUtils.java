package com.cwc.mylibrary.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cwc.mylibrary.Log.MLogHelper;

import com.cwc.mylibrary.contans.Constans;
import com.cwc.mylibrary.http.coreprogress.helper.ProgressHelper;
import com.cwc.mylibrary.http.coreprogress.listener.impl.UIProgressListener;
import com.cwc.mylibrary.model.CommonModel;
import com.cwc.mylibrary.model.ErrModel;
import com.cwc.mylibrary.model.ResultModel;
import com.cwc.mylibrary.model.TokenModel;
import com.cwc.mylibrary.utils.JWTHelper;
import com.cwc.mylibrary.utils.NetHelper;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class MyHttpUtils {
    private static MyHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Handler mDelivery;
    private static Gson mGson;

    private MyHttpUtils() {
        //cookie enabled
//        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static MyHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (MyHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new MyHttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 带进度监听的文件下载
     *
     * @param url
     * @param fileDir                    目标文件夹名
     * @param fileName                   目标文件名
     * @param uiProgressResponseListener 进度监听
     * @param callback                   回调
     */
    public void downloadWithProgress(final String url, final String fileDir, final String fileName, final UIProgressListener uiProgressResponseListener, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(mOkHttpClient, uiProgressResponseListener).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(new ErrModel(-1, Constans.err_msg), callback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file1 = new File(fileDir);//getFileName(url)
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File file = new File(file1, fileName);//getFileName(url)
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(new ErrModel(-1, e.getMessage()), callback);

                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }

        });
    }

    /**
     * 正常下载文件
     *
     * @param url
     * @param fileDir
     * @param fileName
     * @param callback
     */
    public void downloadRes(final String url, final String fileDir, final String fileName, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(new ErrModel(-1, Constans.err_msg), callback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(fileDir, fileName);//getFileName(url)
//                    Log.i("播放资源文件名", getFileName(url));
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(new ErrModel(-1, e.getMessage()), callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }

        });
    }

    /**
     * 适合路径格式为 ：http://xxxx/xxxx?x=x&x=x
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        int separatorIndex1 = path.lastIndexOf("/");
        int separatorIndex2 = path.lastIndexOf("?");
        //path.length()
        return (separatorIndex1 < 0) ? path : path.substring(separatorIndex1 + 1, separatorIndex2);
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param token
     * @param callback
     */
    public void doGet(String url, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(request, callback);
    }

    /**
     * 异步的get请求Html页面内容
     *
     * @param url
     * @param callback
     */
    public void doGetHtml(String url,  final ResultCallback callback) {
        mOkHttpClient = genericClient(false, "");
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult3(request, callback);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param map
     * @param callback
     */
    public void doPost(String url, Map<String, Object> map, String token, final ResultCallback callback) {
        mOkHttpClient = genericClientNew();
        Request request = buildPostRequestNew(url, map);
        deliveryResult2(request, callback);
    }
    public void doPostNew(String url, Map<String, Object> map, String token, final ResultCallback callback) {
        mOkHttpClient = genericClientNew();
        Request request = buildPostRequestNew(url, map);
        deliveryResult(request, callback);
    }
    public void doPost(String url, List<String> keyList, List<String> valueList, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        Request request = buildPostRequestWithList(url, keyList, valueList);
        Headers headers = request.headers();
        deliveryResult2(request, callback);
    }

    /**
     * 异步的put请求
     *
     * @param url
     * @param map
     * @param callback
     */
    public void doPut(String url, Map<String, Object> map, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        Request request = buildPutRequest(url, map);
        deliveryResult(request, callback);
    }

    /**
     * 异步的delete请求
     *
     * @param url
     * @param callback
     */
    public void doDelete(String url, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        Request request = buildDeleteRequest(url);
        deliveryResult(request, callback);
    }

//    /**
//     * 异步基于post的文件上传
//     *
//     * @param url
//     * @param callback
//     * @param files
//     * @param fileKeys
//     * @throws IOException
//     */
//    private void _postAsyn(String url, File[] files, String[] fileKeys, Map<String, String> map, ResultCallback callback) throws IOException {
//        Request request = buildMultipartFormRequest(url, files, fileKeys, map);
//        deliveryResult(request, callback);
//    }

    /**
     * 上传图片（可多张）可带参数 (一组图片)
     *
     * @param url        上传路径
     * @param map        上传参数
     * @param imagePaths 上传图片的原路径
     * @param upLoadName 注意，不是上传图片的名字哦
     * @param callback   回调
     * @throws IOException
     */
    public void upLoadImages(String url, Map<String, String> map, ArrayList<String> imagePaths, String upLoadName, String token, final ResultCallback callback) {
        MLogHelper.i("upLoadImages", imagePaths.toString());
        mOkHttpClient = genericClient(true, token);
        Request request = buildImageMultipartFormRequest(url, map, imagePaths, upLoadName);
        deliveryResult(request, callback);
    }

    public void upLoadVideo(String url, String upLoadName, String videoPath, String token, final ResultCallback callback) {
        MLogHelper.i("upLoadVideo_url", url);
        mOkHttpClient = genericClient(true, token);
        Request request = buildVideoMultipartFormRequest(url, upLoadName, videoPath);
        deliveryResult(request, callback);
    }

//    public void uploadImages(String url, ArrayList<String> imagePaths, String upLoadName,  String token,final ResultCallback callback) {
//        mOkHttpClient = genericClient(true, token);
//        Request request = buildImageMultipartFormRequest(url, null, imagePaths, upLoadName);
//        deliveryResult(request, callback);
//
//    }


    /**
     * 上传多组图片（可带参数）
     *
     * @param url
     * @param paramMap
     * @param imagePathsMap
     * @param callback
     */
    public void upLoadMuchImages(String url, Map<String, String> paramMap, Map<String, List<String>> imagePathsMap, String token, final ResultCallback callback) {
        mOkHttpClient = genericClient(false, token);
        Request request = buildMuchImageMultipartFormRequest(url, paramMap, imagePathsMap);
        deliveryResult(request, callback);
    }

    private Request buildImageMultipartFormRequest(String url, Map<String, String> map, ArrayList<String> imagePaths, String upLoadName) {
//        map = validateParam(map);
//
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
//                    RequestBody.create(null, entry.getValue()));
//        }
        if (imagePaths != null) {
            RequestBody fileBody = null;
            for (String imagePath : imagePaths) {
                File file = new File(imagePath);
                String fileName = file.getName();
                //构建上传文件的类型
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + upLoadName + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private Request buildVideoMultipartFormRequest(String url, String upLoadName, String videoPath) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (!TextUtils.isEmpty(url)) {
            RequestBody fileBody = null;
            File file = new File(videoPath);
            String fileName = file.getName();
            //构建上传文件的类型
            fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
            //TODO 根据文件名设置contentType
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + upLoadName + "\"; filename=\"" + fileName + "\""),
                    fileBody);
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    /**
     * 上传多组图片(带参数)
     *
     * @param url
     * @param paramMap
     * @param imagepathsMap
     * @return
     */
    private Request buildMuchImageMultipartFormRequest(String url, Map<String, String> paramMap, Map<String, List<String>> imagepathsMap) {
        paramMap = validateParam(paramMap);
        imagepathsMap = validateParam2(imagepathsMap);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (paramMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                        RequestBody.create(null, entry.getValue() == null ? "" : entry.getValue()));
            }
        }

        if (imagepathsMap.size() > 0) {
            RequestBody fileBody = null;
            for (Map.Entry<String, List<String>> entry : imagepathsMap.entrySet()) {
                for (String imagePath : entry.getValue()) {
                    File file = new File(imagePath);
                    String fileName = file.getName();
                    //构建上传文件的类型
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                    //TODO 根据文件名设置contentType
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + entry.getKey() + "\"; filename=\"" + fileName + "\""),
                            fileBody);
                }
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 校验参数map是否为空
     *
     * @param map
     * @return
     */
    private Map<String, String> validateParam(Map<String, String> map) {
        if (map == null)
            return new HashMap<String, String>();
        else return map;
    }

    private Map<String, List<String>> validateParam2(Map<String, List<String>> map) {
        if (map == null)
            return new HashMap<String, List<String>>();
        else return map;
    }

    private Request buildPostRequest(String url, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        FormBody.Builder body = new FormBody.Builder();
        body.add("jwt", JWTHelper.encodeJwt(map));
        MLogHelper.i("jwt_body", JWTHelper.encodeJwt(map));
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            body.add(entry.getKey(), entry.getValue());
//        }
        RequestBody requestBody = body.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    private Request buildPostRequestNew(String url, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        FormBody.Builder body = new FormBody.Builder();
//        body.add("jwt", JWTHelper.encodeJwt(map));
//        MLogHelper.i("jwt_body", JWTHelper.encodeJwt(map));
        if (map!=null)
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            body.add(entry.getKey(), entry.getValue()+"");
        }
        RequestBody requestBody = body.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    private Request buildPostRequestWithList(String url, List<String> keyList, List<String> valueList) {
        FormBody.Builder body = new FormBody.Builder();
        for (int i = 0; i < keyList.size(); ++i) {
            body.add(keyList.get(i), valueList.get(i));
        }
        RequestBody requestBody = body.build();

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private Request buildPutRequest(String url, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        FormBody.Builder body = new FormBody.Builder();
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            body.add(entry.getKey(), entry.getValue());
//        }
        body.add("jwt", JWTHelper.encodeJwt(map));
        MLogHelper.i("jwt_body", JWTHelper.encodeJwt(map));
        RequestBody requestBody = body.build();
        return new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();
    }

    private Request buildDeleteRequest(String url) {
        return new Request.Builder()
                .url(url)
                .delete()
                .build();
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(ErrModel em);


        public abstract void onResponse(T response);
    }

    /**
     * 处理访问http结果
     *
     * @param request
     * @param callback
     */
    private void deliveryResult(final Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (!NetHelper.checkNetState(BaseApplication.mContext)) {
//                    sendFailedStringCallback(new ErrModel(-11, Constans.err_net), callback);
//                    return;
//                }
                sendFailedStringCallback(new ErrModel(-1, Constans.err_msg), callback);
                MLogHelper.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                MLogHelper.i("HttpHeaders", headers.toString());

                MLogHelper.i("时间", headers.get("Date"));
                try {
                    final int code = response.code();
                    if (code / 100 == 5) {  //服务器错误
                        sendFailedStringCallback(new ErrModel(500, Constans.err_data), callback);
                    } else {
                        final String body = response.body().string();
                            JSONObject jsonObject = new JSONObject(body);
                           String s =  jsonObject.toString();
                        MLogHelper.i("body", s);
                        String path = request.url().url().getPath();
                        MLogHelper.i("body_666",s);

//                        final Headers header = response.headers();
                        if (code / 100 == 4) {  //错误
//                            MLogHelper.i("message", response.message());
                            try {
                                ErrModel em = mGson.fromJson(body, ErrModel.class);
                                sendFailedStringCallback(em, callback);
                            } catch (Exception e) {
                                String s2 = request.url().url().getPath();
                            }
                        } else {  //成功
                            try {
//                                TokenModel tm = mGson.fromJson(body, TokenModel.class);
//
//                                MLogHelper.i("成功", JWTHelper.parseJwt(tm.getToken()));
//                                ResultModel rm = mGson.fromJson(JWTHelper.parseJwt(tm.getToken()), ResultModel.class);
//
                                ResultModel model = new ResultModel();
                                Gson gson = new Gson();
                                CommonModel obj = gson.fromJson(s, CommonModel.class);
                                if ("failure".equals(obj.getStatus())) {
                                    ErrModel errModel = new ErrModel(0, obj.getInfo());
                                    sendFailedStringCallback(errModel, callback);
                                    return;
                                }
                                model.setData(obj.getData());
                                sendSuccessResultCallback(model, callback);
                            } catch (Exception e) {
                                String eception = e.getMessage();
                                String str = body;
                               String str2 =  request.url().url().getPath();
                            }
                        }
                    }
                } catch (Exception e) {
                    MLogHelper.i("error", e.getMessage());

                    sendFailedStringCallback(new ErrModel(-1, e.getMessage()), callback);
                }
            }

        });
    }
    private void deliveryResult2(final Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (!NetHelper.checkNetState(BaseApplication.mContext)) {
//                    sendFailedStringCallback(new ErrModel(-11, Constans.err_net), callback);
//                    return;
//                }
                sendFailedStringCallback(new ErrModel(-1, Constans.err_msg), callback);
                MLogHelper.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (request.url().url().getPath().contains("/malls/orders")) {
                    String s = "";
                    String path = request.url().url().getPath();
                }
                Headers headers = response.headers();
                MLogHelper.i("Headers", headers.toString());


                MLogHelper.i("时间", headers.get("Date"));
                try {
                    final int code = response.code();
                    if (code / 100 == 5) {  //服务器错误
                        sendFailedStringCallback(new ErrModel(500, Constans.err_data), callback);
                    } else {
                        final String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        String s =  jsonObject.toString();
                        MLogHelper.i("body", s);
                        String path = request.url().url().getPath();
                        MLogHelper.i("body_666",s);

//                        final Headers header = response.headers();
                        if (code / 100 == 4) {  //错误
//                            MLogHelper.i("message", response.message());
                            try {
                                ErrModel em = mGson.fromJson(body, ErrModel.class);
                                sendFailedStringCallback(em, callback);
                            } catch (Exception e) {
                                String s2 = request.url().url().getPath();
                            }
                        } else {  //成功
                            try {
//                                TokenModel tm = mGson.fromJson(body, TokenModel.class);
//
//                                MLogHelper.i("成功", JWTHelper.parseJwt(tm.getToken()));
//                                ResultModel rm = mGson.fromJson(JWTHelper.parseJwt(tm.getToken()), ResultModel.class);
//
                                ResultModel model = new ResultModel();
                                Gson gson = new Gson();
                                CommonModel obj = gson.fromJson(s, CommonModel.class);
                                if ("failure".equals(obj.getStatus())) {
                                    ErrModel errModel = new ErrModel(0, obj.getInfo());
                                    sendFailedStringCallback(errModel, callback);
                                    return;
                                }
                                model.setData(new Gson().toJson(obj.getData()));
                                sendSuccessResultCallback(model, callback);
                            } catch (Exception e) {
                                String eception = e.getMessage();
                                String str = body;
                                String str2 =  request.url().url().getPath();
                            }
                        }
                    }
                } catch (Exception e) {
                    MLogHelper.i("error", e.getMessage());

                    sendFailedStringCallback(new ErrModel(-1, e.getMessage()), callback);
                }
            }

        });
    }

    private void deliveryResult3(final Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                if (!NetHelper.checkNetState(BaseApplication.mContext)) {
//                    sendFailedStringCallback(new ErrModel(-11, Constans.err_net), callback);
//                    return;
//                }
                sendFailedStringCallback(new ErrModel(-1, Constans.err_msg), callback);
                MLogHelper.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (request.url().url().getPath().contains("/malls/orders")) {
                    String s = "";
                    String path = request.url().url().getPath();
                }
                Headers headers = response.headers();
                MLogHelper.i("Headers", headers.toString());


                MLogHelper.i("时间", headers.get("Date"));
                try {
                    final int code = response.code();
                    if (code / 100 == 5) {  //服务器错误
                        sendFailedStringCallback(new ErrModel(500, Constans.err_data), callback);
                    } else {
                        final String body = response.body().string();
                        MLogHelper.i("body", body);

                        if (code / 100 == 4) {  //错误
//                            MLogHelper.i("message", response.message());
                            try {
                                ErrModel em = mGson.fromJson(body, ErrModel.class);
                                sendFailedStringCallback(em, callback);
                            } catch (Exception ignored) {

                            }
                        } else {  //成功
                            try {
                                ResultModel model = new ResultModel();
                                CommonModel obj = new CommonModel(body);
                                model.setData(obj.getData());
                                sendSuccessResultCallback(model, callback);
                            } catch (Exception ignored) {

                            }
                        }
                    }
                } catch (Exception e) {
                    MLogHelper.i("error", e.getMessage());
                    sendFailedStringCallback(new ErrModel(-1, e.getMessage()), callback);
                }
            }

        });
    }



    private void sendFailedStringCallback(final ErrModel em, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(em);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null && object != null) {
                    callback.onResponse(object);
                }
            }
        });
    }
    public OkHttpClient genericClientNew() {
        mOkHttpClient = new OkHttpClient.Builder()

                //其他配置
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request request = chain.request()
                                .newBuilder()
                                .build();

                        return chain.proceed(request);
                    }
                })
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();
        return mOkHttpClient;

    }

    public OkHttpClient genericClient(final boolean isUpLoad, final String token) {
        mOkHttpClient = new OkHttpClient.Builder()

                //其他配置
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String content_type = null;
                        if (isUpLoad) {  //设置是否是正常请求还是上传请求
                            content_type = "multipart/form-data; charset=utf-8";
                        } else {
                            content_type = "application/x-www-form-urlencoded; charset=utf-8";
                        }

                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", content_type)
                                .addHeader("Accept-Encoding", "identity")

                                //等以后开具体项目再打开，因为user model属于业务层，得具体情况具体定义
                                .addHeader("Authorization", token)

//                                .addHeader("uid", user.getUser_id() != null ? user.getUser_id() : "")
                                .build();

                        return chain.proceed(request);
                    }
                })
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();
        return mOkHttpClient;

    }
    /*
     * unicode编码转中文
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
}
