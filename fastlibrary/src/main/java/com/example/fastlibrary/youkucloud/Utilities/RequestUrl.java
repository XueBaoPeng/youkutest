package com.example.fastlibrary.youkucloud.Utilities;

public enum RequestUrl {
	ROUTER(1, "https://openapi.youku.com/router/rest.json");

	private int code;
	private String url;

	RequestUrl(int code, String url) {
		this.setCode(code);
		this.setUrl(url);
	}

	private void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	private void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
