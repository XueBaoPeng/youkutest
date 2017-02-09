package com.example.fastlibrary.youkucloud.Module;


import com.example.fastlibrary.youkucloud.Common.Request;
import com.example.fastlibrary.youkucloud.Utilities.ErrorCode;

import java.util.TreeMap;

public abstract class Base {
	protected String appKey = "";
	protected String appSecret = "";
	protected String requestMethod = "";

	public void setConfig(TreeMap<String, Object> config) throws Exception{
		String value = null;
		for (String key : config.keySet()) {
			value = config.get(key).toString();
			switch (key) {
			case "AppKey":
				setConfigAppKey(value);
				break;

			case "AppSecret":
				setConfigAppSecret(value);
				break;

			case "RequestMethod":
				setRequestMethod(value);
				break;

			default:
				break;
			}
		}
		if("".equals(appKey)) {
			throw new Exception(ErrorCode.APP_KEY_MISSING.getDesc());
		}
		if("".equals(appSecret)) {
			throw new Exception(ErrorCode.APP_SECRET_MISSING.getDesc());
		}
		if("".equals(requestMethod)) {
			requestMethod = "POST";
		}
	}

	private void setConfigAppKey(String appKey) {
		this.appKey = appKey;
	}

	private void setConfigAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public String call(TreeMap<String, Object> params, String fileName){
		String response = Request.requestUrl(params, requestMethod, fileName);
		return response;
	}
}
