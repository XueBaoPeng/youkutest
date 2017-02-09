package com.example.fastlibrary.youkucloud.Common;

import com.example.fastlibrary.youkucloud.Utilities.ErrorCode;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @brief 请求调用类
 * @author luhanlin
 */
public class Request {
    private static HttpClient httpClient = null;

    static {
        try{
            httpClient = HttpClients.createDefault();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String requestUrl(Map<String, Object> requestParams, String requestMethod, String fileName) {
        String url = requestParams.get("requestUrl").toString();
        requestParams.remove("requestUrl");
        if(requestMethod.toLowerCase().equals("post")) {
            return sendPostRequest(url, requestParams, fileName);
        } else {
            return sendGetRequest(url, requestParams);
        }
    }

    private static String sendGetRequest(String url, Map<String, Object> requestParams) {
        StringBuffer urlStr = new StringBuffer(url);
        urlStr.append("?");
        try{
            boolean isNotFirst = false;
            for(Map.Entry<String, Object> entry: requestParams.entrySet()) {
                if (isNotFirst) {
                    urlStr.append('&');
                } else {
                    isNotFirst = true;
                }
                urlStr.append(entry.getKey());
                urlStr.append('=');
                urlStr.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(urlStr.toString());
        return excute(null, httpGet);
    }

    private static String sendPostRequest(String url, Map<String, Object> requestParams, String fileName) {
        HttpPost httpPost = new HttpPost(url);

        if(fileName != null) {
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(fileName));
            reqEntity.addPart("file", fileBody);

            StringBody strBody = null;
            for(Map.Entry<String, Object> entry: requestParams.entrySet()) {
                strBody = new StringBody(String.valueOf(entry.getValue()), ContentType.create("text/plain", Consts.UTF_8));
                reqEntity.addPart(entry.getKey(), strBody);
            }

            httpPost.setEntity(reqEntity.build());
        } else {
            List<NameValuePair> nameValuePairList = new LinkedList<NameValuePair>();
            NameValuePair nameValuePair = null;
            for(Map.Entry<String, Object> entry: requestParams.entrySet()) {
                nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
                nameValuePairList.add(nameValuePair);
            }
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(nameValuePairList,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpPost.setEntity(entity);
        }
        return excute(httpPost, null);
    }

    private static String excute(HttpPost httpPost, HttpGet httpGet) {
        StringBuffer result = new StringBuffer();
        HttpResponse response = null;
        try {
            if(httpPost == null) {
                response = httpClient.execute(httpGet);
            } else {
                response = httpClient.execute(httpPost);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = new StringBuffer("{\"e\":{\"code\":");
            result.append(ErrorCode.REQUEST_FAILED.getCode());
            result.append(",\"desc\":\"");
            result.append(ErrorCode.REQUEST_FAILED.getDesc() + ", " + e.toString().replace("\"", ""));
            result.append("\"}}");
            return result.toString();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity responseEntity = response.getEntity();
        if(statusCode != HttpStatus.SC_OK){
            result = new StringBuffer("{\"e\":{\"code\":");
            result.append(statusCode);
            result.append(",\"desc\":\"");
            result.append("访问失败");
            result.append("\"}}");
        }
        try {
            result.append(EntityUtils.toString(responseEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            if(httpPost != null) {
//                httpPost.releaseConnection();
//            }
//            if(httpGet != null) {
//                httpGet.releaseConnection();
//            }
        }

        return result.toString();
    }
}
