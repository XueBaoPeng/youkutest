package com.example.fastlibrary.youkucloud.Utilities;

/**
 * @brief SDK错误码
 * @author luhanlin
 */
public enum ErrorCode {
    SUCCESS(0, "success"),
    DATA_MISSING(-2000, "系统参数不能为空"),
    APP_KEY_MISSING(-2001, "App Key不能为空"),
    APP_SECRET_MISSING(-2002, "App Secret不能为空"),
    REQUEST_URL_MISSING(-2003, "访问url不能为空!"),
    FILE_MISSING(-2004, "文件路径参数不能为空!"),
    REQUEST_FAILED(-1000, "request failed!"),
    FILE_NOT_FOUND(-1001, "file not found!"),
    U8_ENCODE_ERROR(-1002, "paramter can't convert to UTF-8!"),
    ILLEGAL_FILE_TYPE(-1003, "file type is illegal, Audio/Video only!");

    private int code;
    private String desc;

    ErrorCode(int code, String desc) {
        this.setCode(code);
        this.setDesc(desc);
    }

    private void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}