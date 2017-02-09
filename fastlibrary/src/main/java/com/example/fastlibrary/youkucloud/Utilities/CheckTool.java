package com.example.fastlibrary.youkucloud.Utilities;

import java.util.HashMap;
import java.util.Map;

public class CheckTool {
    private final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    /*-----------------------------目前常见音频、视频后缀名及MIMEType----------------------------*/
    static {
        //音频
        FILE_TYPE_MAP.put("mp3", "audio/mpeg");
        FILE_TYPE_MAP.put("mid", "audio/midi");
        FILE_TYPE_MAP.put("ogg", "audio/ogg");
        FILE_TYPE_MAP.put("mp4a", "audio/mp4");
        FILE_TYPE_MAP.put("wav", "audio/wav");
        FILE_TYPE_MAP.put("wma", "audio/x-ms-wma");
        FILE_TYPE_MAP.put("au", "audio/basic");
        FILE_TYPE_MAP.put("snd", "audio/basic");
        FILE_TYPE_MAP.put("rmi", "audio/mid");
        FILE_TYPE_MAP.put("aif", "audio/x-aiff");
        FILE_TYPE_MAP.put("aifc", "audio/x-aiff");
        FILE_TYPE_MAP.put("aiff", "audio/x-aiff");
        FILE_TYPE_MAP.put("m3u", "audio/x-mpegurl");
        FILE_TYPE_MAP.put("ra", "audio/x-pn-realaudio");
        FILE_TYPE_MAP.put("ram", "audio/x-pn-realaudio");

        //视频
        FILE_TYPE_MAP.put("avi", "video/x-msvideo");
        FILE_TYPE_MAP.put("dv", "video/x-dv");
        FILE_TYPE_MAP.put("mp4", "video/mp4");
        FILE_TYPE_MAP.put("mpeg", "video/mpeg");
        FILE_TYPE_MAP.put("mpg", "video/mpeg");
        FILE_TYPE_MAP.put("mov", "video/quicktime");
        FILE_TYPE_MAP.put("wm", "video/x-ms-wmv");
        FILE_TYPE_MAP.put("flv", "video/x-flv");
        FILE_TYPE_MAP.put("mkv", "video/x-matroska");
        FILE_TYPE_MAP.put("mp2", "video/mpeg");
        FILE_TYPE_MAP.put("mpa", "video/mpeg");
        FILE_TYPE_MAP.put("mpe", "video/mpeg");
        FILE_TYPE_MAP.put("mpeg", "video/mpeg");
        FILE_TYPE_MAP.put("mpg", "video/mpeg");
        FILE_TYPE_MAP.put("mpv2", "video/mpeg");
        FILE_TYPE_MAP.put("qt", "video/quicktime");
        FILE_TYPE_MAP.put("lsf", "video/x-la-asf");
        FILE_TYPE_MAP.put("lsx", "video/x-la-asf");
        FILE_TYPE_MAP.put("asf", "video/x-ms-asf");
        FILE_TYPE_MAP.put("asr", "video/x-ms-asf");
        FILE_TYPE_MAP.put("asx", "video/x-ms-asf");
        FILE_TYPE_MAP.put("movie", "video/x-sgi-movie");
        FILE_TYPE_MAP.put("rmvb", "video/vnd.rn-realvideo");
    }

    /**
     * 通过文件后缀名校验文件格式是否合法
     * @param fileType，文件后缀名
     * @return
     */
    public static boolean checkFileTypeIsValid(String fileType) {
        boolean isValid = false;
        if(FILE_TYPE_MAP.containsKey(fileType)) {
            isValid = true;
        }
        return isValid;
    }
}
