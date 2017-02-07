package com.vod.player.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetCodeActivity extends Activity {
        private WebView webview;
        private String strCode;
        private String access_token;
        private String CLIENT_ID = "df5ed5e681642009";
        private String CLIENT_SECRET = "e877b34186827ccb2d40aba1a332cc31";
        private String TAG="GetCodeActivity";
        private String REDIRECT_URL = "http://1sheng1shi.blog.51cto.com";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_get_code);
            // 设置Web视图
            webview =(WebView)findViewById(R.id.webView1);
            if(webview == null){
                return;
            }

            //下面这个链接大家可只要把client_id及redirect_uri后面的值换成我们申请优酷开发者帐号时优酷提供的及我们设置的回调地址就可以
            String url = "https://openapi.youku.com/v2/oauth2/authorize?client_id="+CLIENT_ID+"&response_type=code&redirect_uri="+REDIRECT_URL+"&state=xyz";
            webview.loadUrl(url);
            //https://api.weibo.com/oauth2/default.html
            Log.d(TAG, "load before="+url);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    String[] str = null;
                    str = url.split("=")[1].split("&");
                    strCode = str[0];
                    Intent it = new Intent();
                    it.putExtra("code", strCode);
                    it.putExtra("access_token", getToken());
                    Log.d(TAG, "code="+strCode+ " status="+str[1]+" "+url +" token="+getToken());

//                     Toast.makeText(getApplicationContext(), strCode, 0).show();
                    it.setClass(GetCodeActivity.this, MainActivity.class);
                    startActivity(it);
                    return false;
                }
            });
        }
        private String getToken() {
            // TODO Auto-generated method stub
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    String strUrl = "https://openapi.youku.com/v2/oauth2/token";
                    HttpResponse httpResponse = null;
                    HttpPost httpPost = new HttpPost(strUrl);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("client_id", CLIENT_ID));//你申请优酷的client_id
                    params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));//你申请优酷的client_secret
                    params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                    params.add(new BasicNameValuePair("code", strCode));//刚刚获取到的code

                    params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URL));//回调地址，必须跟网站的一样
                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        Log.d(TAG,"getToken () setEntity after");

                        httpResponse = new DefaultHttpClient().execute(httpPost);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(httpResponse.getEntity());
                            Log.d(TAG,"result="+ result );
                            System.out.println(result);
                            JSONObject object = new JSONObject(result);
                            access_token = object.getString("access_token");//access_token获取成功

//                          Toast.makeText(getApplicationContext(), "access_token="+access_token, 0).show();
//                          return access_token;
                        }else{
                            Log.d(TAG,"getToken() getStatusCode="+ httpResponse.getStatusLine().getStatusCode());
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d(TAG,"Exception="+ e.toString());
                    }
                }
            });
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d(TAG, "access_token="+access_token);
            return access_token;
        }
        @Override
        protected void onDestroy() {
            // TODO Auto-generated method stub
            if(webview != null){
                webview = null;
            }
            super.onDestroy();
        }

    }
