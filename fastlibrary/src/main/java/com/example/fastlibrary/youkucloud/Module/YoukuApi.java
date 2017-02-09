package com.example.fastlibrary.youkucloud.Module;


import com.example.fastlibrary.youkucloud.Common.Sign;
import com.example.fastlibrary.youkucloud.Utilities.ErrorCode;
import com.example.fastlibrary.youkucloud.Utilities.RequestUrl;

import java.util.TreeMap;

public class YoukuApi extends Base {

    public YoukuApi(TreeMap<String, Object> params) throws Exception{
        if(params == null) {
            throw new Exception(ErrorCode.DATA_MISSING.getDesc());
        }
        super.setConfig(params);
    }

    public String callApi(TreeMap<String, Object> params) throws Exception{
        params = Sign.get_sign(params, this.appKey, this.appSecret);
        if (params.containsKey("error")) {
            String code = params.get("error").toString();
            return "{\"e\":{\"code\":" + code + ",\"desc\":\"" + ErrorCode.valueOf(code) + "\"}}";
        }
        params.put("requestUrl", RequestUrl.ROUTER.getUrl());
        return call(params, null);
    }

    public String uploadVodFile(TreeMap<String, Object> params) {
        if(!params.containsKey("requestUrl")) {
            return "{\"e\":{\"code\":" + ErrorCode.REQUEST_URL_MISSING.getCode() + ",\"desc\":\"" + ErrorCode.REQUEST_URL_MISSING.getDesc() + "\"}}";
        }
        if(!params.containsKey("file")) {
            return "{\"e\":{\"code\":" + ErrorCode.FILE_MISSING.getCode() + ",\"desc\":\"" + ErrorCode.FILE_MISSING.getDesc() + "\"}}";
        }
        String fileName = params.get("file").toString();
        params.remove("file");
        return call(params, fileName);
    }
}
