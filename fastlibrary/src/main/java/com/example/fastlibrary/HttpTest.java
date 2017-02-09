package com.example.fastlibrary;

import com.example.fastlibrary.listener.HttpResonCallback;
import com.example.fastlibrary.request.RequestParms;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class HttpTest  {


     public static void main(String args[]){
         OkHttpClient okHttpClient1=OkHttpClient.getInstance();
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("username", "13519297683@163.com");
          params.put("type", "Tenbre");
         params.put("pwd", "aaaaaa");
         params.put("timeZoneID", "Asia/Shanghai");
         params.put("deviceID", "860735030814982");
         params.put("versionCode", "113");
         okHttpClient1.post("http://10.0.63.242:8281/cms/login", new RequestParms(params), new HttpResonCallback() {
             @Override
             public void onSuccess(Object response) {
                 System.out.print(response);
             }

             @Override
             public void onFailure(Object error) {
                 System.out.print(error.toString());
             }
         });
    }
}
