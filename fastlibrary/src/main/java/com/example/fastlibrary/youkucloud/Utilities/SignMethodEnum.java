package com.example.fastlibrary.youkucloud.Utilities;


/**
 * @brief 加密类型
 * @author luhanlin
 */
public enum SignMethodEnum {
    HMAC("hmac"), HMACMD5("HmacMD5"), HMACSHA1("HmacSHA1"), HMACSHA256("HmacSHA256"),
    HMACSHA384("HmacSHA384"), HMACSHA512("HmacSHA512"), MD5("md5");

    private String value;

    SignMethodEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static boolean isValid(String value) {
        for (SignMethodEnum e : SignMethodEnum.values()) {
            if (e.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHmac(String value) {
        if (null != value && value.toLowerCase().startsWith("hmac")) {
            return true;
        }
        return false;
    }
}
