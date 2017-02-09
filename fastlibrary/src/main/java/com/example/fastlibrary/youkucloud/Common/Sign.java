package com.example.fastlibrary.youkucloud.Common;
import com.example.fastlibrary.youkucloud.Utilities.ErrorCode;
import com.example.fastlibrary.youkucloud.Utilities.MD5;
import com.example.fastlibrary.youkucloud.Utilities.SignMethodEnum;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class Sign {

    /**
     * @brief 签名
     * @author luhanlin
     * @date 2016-05-12
     *
     * @param params 需加密的参数
     * @param appKey 代理层获取密钥
     * @param secret 加密密钥
     *
     * @return
     */
    public static TreeMap<String, Object> get_sign(TreeMap<String, Object> params, String appKey, String secret) throws Exception {
        params.put("client_id", appKey);
        params.put("timestamp", System.currentTimeMillis() / 1000);
        params.put("version", "3.0");
        String signMethod = params.get("sign_method") == null ? null : params.get("sign_method").toString();
        if(signMethod == null || "".equals(signMethod)) {
            signMethod = SignMethodEnum.MD5.getValue();
            params.put("sign_method", "md5");
        }

        StringBuffer signString = new StringBuffer();
        try {
            for(Map.Entry<String, Object> entry : params.entrySet()) {
                signString.append(entry.getKey());
                signString.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception(ErrorCode.U8_ENCODE_ERROR.getDesc());
        }

        String sign = "";
        if(SignMethodEnum.isHmac(signMethod)){
            try {
                sign = hmacSign(secret, signMethod, signString.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }else{
            signString.append(secret);
            try {
                sign = md5Sign(signString.toString());
            } catch(Exception e) {
                params.put("error", ErrorCode.U8_ENCODE_ERROR.getCode());
                return params;
            }
        }
        return packageRequestParams(params, appKey, sign);
    }

    private static TreeMap<String, Object> packageRequestParams(TreeMap<String, Object> params, String appKey, String sign) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("\"client_id\":");
        buffer.append("\"");
        buffer.append(appKey);
        buffer.append("\",");
        buffer.append("\"timestamp\":");
        buffer.append("\"");
        buffer.append(params.get("timestamp"));
        buffer.append("\",");
        buffer.append("\"version\":");
        buffer.append("\"3.0\",");
        buffer.append("\"sign_method\":");
        buffer.append("\"");
        buffer.append(params.get("sign_method"));
        buffer.append("\",");
        buffer.append("\"sign\":");
        buffer.append("\"");
        buffer.append(sign);
        buffer.append("\",");
        buffer.append("\"action\":");
        buffer.append("\"");
        buffer.append(params.get("action"));
        buffer.append("\"}");
        params.put("opensysparams", buffer.toString());

        params.remove("client_id");
        params.remove("timestamp");
        params.remove("version");
        params.remove("sign_method");
        params.remove("action");
        params.remove("sign");
        return params;
    }

    private static String hmacSign(String secret, String signMethod, String signString) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String algorithm = null;
        if(SignMethodEnum.HMAC.getValue().equals(signMethod)){
            algorithm = SignMethodEnum.HMACMD5.getValue();
        }else{
            algorithm = signMethod;
        }
        SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(signString.getBytes("UTF-8"));
        return byte2hex(bytes);
    }

    private static String md5Sign(String signString)  {
        String sign = MD5.stringToMD5(signString);
        return sign;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }
}
