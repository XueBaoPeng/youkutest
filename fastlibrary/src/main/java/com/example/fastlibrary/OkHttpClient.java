package com.example.fastlibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.fastlibrary.https.HttpsUtils;
import com.example.fastlibrary.listener.HttpResonCallback;
import com.example.fastlibrary.request.RequestParms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ****************************************
 * Created by xbp on 2017/2/8.
 * ****************************************
 */
public class OkHttpClient {
    private static final  int TIME_OUT=5;
    private static okhttp3.OkHttpClient okHttpClient;
    private static OkHttpClient mInstance;
//    private Context mContext;
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static Handler mainThreadHandler;//主线程handler，回传到主线程数据
    //单利模式减少创建消耗
    public static OkHttpClient getInstance(){
        if(mInstance==null){
            synchronized (OkHttpClient.class){
                mInstance=new OkHttpClient( );
            }
        }
//        mInstance.mContext=context;
        return mInstance;
    }

    public OkHttpClient() {
        okhttp3.OkHttpClient.Builder okHttpBuilder = new okhttp3.OkHttpClient.Builder();
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.followRedirects(true); // 允许自定项
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager); // 信任所有ssl证书
        okHttpClient=okHttpBuilder.build();
//        mainThreadHandler=new Handler(Looper.getMainLooper());
    }

    /**
     * 设置请求头
     * @param headersParams
     * @return
     */
    private Headers SetHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }


    public static void get(String url, final HttpResonCallback httpResonCallback){
        try {
            Request request=new Request.Builder().url(url).get().build();
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    httpResonCallback.onFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    httpResonCallback.onSuccess(response.body().string());
                }
            });
        }catch (final Exception e){
//            mainThreadHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    httpResonCallback.onFailure(e);
//                }
//            });
        }
    }

    public static void post(String url, RequestParms requestParms, final HttpResonCallback httpResonCallback){
        FormBody.Builder builder=new FormBody.Builder();
        if(requestParms!=null){
            for (Map.Entry<String,String> entry:requestParms.urlParams.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        FormBody formBody=builder.build();
        RequestBody requestBody=RequestBody.create(MEDIA_TYPE_JSON,formBody.toString());
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("token", "c02cd563764583a17aa2b08aeed6107e");
            params.put("lnCode", "en");
            params.put("appVersion", "113");
            Request request=new Request.Builder().url(url).post(requestBody).header("token", "c02cd563764583a17aa2b08aeed6107e").build();
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    httpResonCallback.onFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    httpResonCallback.onSuccess(response.body().string());
                }
            });

        }catch (final Exception e){
//            mainThreadHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    httpResonCallback.onFailure(e);
//                }
//            });
        }
    }

}
