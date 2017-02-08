package com.example.fastlibrary.request;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ****************************************
 * Created by xbp on 2017/2/8.
 * 网络请求的参数
 * ****************************************
 */
public class RequestParms {
    public ConcurrentHashMap<String,String> urlParams=new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,Object> failParams=new ConcurrentHashMap<>();

    public RequestParms(Map<String,Object> source){
        if(source!=null){
            for (Map.Entry<String,Object> entry:source.entrySet()){
                if(entry.getValue() instanceof File){
                    put(entry.getKey(), entry.getValue());
                }else {
                    put(entry.getKey(), (String) entry.getValue());
                }
            }
        }
    }

    private void put(String name,String value){
        if(name==null) return;
        urlParams.put(name,value);
    }

    private void put(String name,Object value){
        if(name==null) return;
        failParams.put(name,value);
    }

}
