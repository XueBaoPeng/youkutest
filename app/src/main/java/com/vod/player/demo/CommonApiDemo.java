package com.vod.player.demo;

import com.example.fastlibrary.youkucloud.Module.YoukuApi;
import com.example.fastlibrary.youkucloud.Utilities.Json.JSONException;
import com.example.fastlibrary.youkucloud.Utilities.Json.JSONObject;
import com.example.fastlibrary.youkucloud.YoukuApiModuleCenter;

import java.util.TreeMap;

public class CommonApiDemo {
	public static void main(String[] args) {

        TreeMap<String, Object> config = new TreeMap<String, Object>();
        config.put("AppKey", "");
        config.put("AppSecret", "");
        config.put("RequestMethod", "POST");
        String resultStr = null;
		try{
            YoukuApiModuleCenter module = new YoukuApiModuleCenter(new YoukuApi(config));
//            可使用setRequestMethod设置访问方式（POST、GET）
//            module.setRequestMethod("GET");
			System.out.println("starting...");
			//配置业务参数及相应action
			TreeMap<String, Object> params = new TreeMap<String, Object>();
            params.put("action", "");
            /**
             * 可不填，默认md5加密
             */
            params.put("sign_method", "HmacSHA256");

            resultStr = module.call("callApi", params);
            JSONObject resultObj = new JSONObject(resultStr);
			//返回结果中code不为0则说明获取失败
            if(resultObj.has("e")) {
                if(resultObj.getJSONObject("e").getInt("code") != 0) {
                    //打印失败原因
                    System.out.println(resultObj);
                } else {
                    System.out.println(resultObj);
                }
            } else {
                System.out.println(resultObj);
            }
			System.out.println("end...");
		} catch (JSONException e) {
            System.out.println(resultStr);
            System.out.println(e);
            System.out.println("error...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error...");
        }
    }
}
