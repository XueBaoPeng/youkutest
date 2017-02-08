package com.example.fastlibrary.listener;

/**
 * ****************************************
 * Created by xbp on 2017/2/8.
 * 网络请求结果的回调
 * ****************************************
 */
public interface HttpResonCallback {

    void  onSuccess(Object response);

    void  onFailure(Object error);

}
